package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.News
import com.sansantek.sansanmulmul.data.model.Recommendation
import com.sansantek.sansanmulmul.databinding.FragmentHomeTabBinding
import com.sansantek.sansanmulmul.ui.adapter.FirstRecommendationViewPagerAdapter
import com.sansantek.sansanmulmul.ui.adapter.NewsViewPagerAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.HorizontalMarginItemDecoration
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.newsService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainSearchViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

private const val TAG = "HomeTabFragment 싸피"

class HomeTabFragment : BaseFragment<FragmentHomeTabBinding>(
    FragmentHomeTabBinding::bind,
    R.layout.fragment_home_tab
) {
    private lateinit var searchEditTextView: EditText
    private val searchViewModel: MountainSearchViewModel by activityViewModels()
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private lateinit var newsList: List<News>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        // 뉴스 데이터를 가져온 후 ViewPager를 초기화합니다.
        setNewsData {
            initNewsViewPager(binding.layoutCarouselNews, it)
        }
        loadSeasonalRecommendations()
    }

    private fun init() {
        searchEditTextView = binding.includeEditText.etSearch

        loadUserProfile()

        // 검색 완료 시 프래그먼트 이동
        searchEditTextView.setOnEditorActionListener(OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val searchKeyword = searchEditTextView.text.toString()
                searchViewModel.setSearchKeyword(searchKeyword)
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_view, MountainSearchResultFragment()).commit()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun loadUserProfile() {
        sharedPreferencesUtil.getKakaoLoginToken()?.let {
            lifecycleScope.launch {
                val user = userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
                if (user.code() == 200) {
                    user.body()?.let {
                        binding.tvShared.text = it.userNickName
                    }
                } else {
                    Log.d(TAG, "loadUserProfile: result :${user.code()}")
                    Log.d(TAG, "loadUserProfile: ${user.body()}")
                }
            }
        }
    }

    private fun initNewsViewPager(viewPager: ViewPager2, itemList: List<News>) {
        val adapter = NewsViewPagerAdapter(itemList)
        viewPager.adapter = adapter

        viewPager.offscreenPageLimit = 1
        viewPager.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val itemDecoration = HorizontalMarginItemDecoration(horizontalMarginInDp = 20)
        viewPager.addItemDecoration(itemDecoration)
        viewPager.setPageTransformer { page, position ->
            val offsetX = position * -(2 * 100) // offset 값으로 간격 조정
            page.translationX = offsetX

            val scale = 1 - abs(position) // scale 값으로 양쪽 애들 높이 조정
            page.scaleY = 0.85f + 0.15f * scale
        }
        autoScroll(viewPager, 5000)
    }

    private fun initRecommendationViewPager(
        viewPager: ViewPager2,
        recommendationList: List<Recommendation>,
        autoScrollDelay: Long
    ) {
        val adapter = FirstRecommendationViewPagerAdapter(
            recommendationList,
            object : FirstRecommendationViewPagerAdapter.OnItemClickListener {
                override fun onItemClick(item: Recommendation) {
                    // 뷰모델을 가져와서 산 ID 넘겨주기
                    mountainDetailViewModel.setMountainID(item.mountainId)

                    changeFragmentWithPopUpAnimation(MountainDetailFragment())
                }
            })
        viewPager.adapter = adapter

        // 중간 아이템부터 보이도록
        val middlePosition = Int.MAX_VALUE / 2
        val initialPosition = middlePosition - middlePosition % adapter.items.size
        viewPager.setCurrentItem(initialPosition, false)

        // 양쪽 보이게
        viewPager.offscreenPageLimit = 1
        val itemDecoration = HorizontalMarginItemDecoration(horizontalMarginInDp = 20)
        viewPager.addItemDecoration(itemDecoration)
        viewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * 180) // offset 값으로 간격 조정
            page.translationX = offset
        }
        autoScroll(viewPager, autoScrollDelay)
    }

    private fun setNewsData(onNewsDataReady: (List<News>) -> Unit) {
        lifecycleScope.launch {
            newsList = newsService.getNewsKeyword("가리산")
            Log.d(TAG, "setNewsData: ${newsList}")

            // 각 뉴스 항목에 산 이미지를 설정
            val updatedNewsList = newsList.map { news ->
                val mountainImg = getMountainImage(news.mountainName)
                news.copy(mountainImg = mountainImg)
            }

            // 비동기 처리 후 결과를 콜백으로 전달
            onNewsDataReady(updatedNewsList)
        }
    }

    private suspend fun getMountainImage(mountainName: String): String {
        val response = mountainService.searchMountainList(mountainName)
        return if (response.isSuccessful) {
            val mountainList = response.body() ?: emptyList()
            if (mountainList.isNotEmpty()) {
                mountainList[0].mountainImg ?: "@drawable/default_mountain" // 기본 이미지 URL
            } else {
                "@drawable/default_mountain" // 기본 이미지 URL
            }
        } else {
            "@drawable/default_mountain" // 기본 이미지 URL
        }
    }

    private fun loadSeasonalRecommendations() {
        loadRecommendationData(binding.vpRecommendation1, "spring", 5000)
        loadRecommendationData(binding.vpRecommendation2, "summer", 5000)
        loadRecommendationData(binding.vpRecommendation3, "fall", 5000)
        loadRecommendationData(binding.vpRecommendation4, "winter", 5000)
    }

    private fun loadRecommendationData(
        viewPager: ViewPager2,
        season: String,
        autoScrollDelay: Long
    ) {
        lifecycleScope.launch {
            val recommendationList = when (season) {
                "spring" -> mountainService.getMountainSpring()
                "summer" -> mountainService.getMountainSummer()
                "fall" -> mountainService.getMountainFall()
                "winter" -> mountainService.getMountainWinter()
                else -> emptyList()
            }.map {
                Recommendation(it.mountainId, it.mountainName, it.mountainHeight, it.mountainImg)
            }

            // 계절별 데이터를 로그로 출력
            Log.d(TAG, "Season - $season, Data - $recommendationList")

            initRecommendationViewPager(viewPager, recommendationList, autoScrollDelay)
        }
    }


    private fun autoScroll(viewPager: ViewPager2, delay: Long) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                viewPager.currentItem += 1
                handler.postDelayed(this, delay) // 스크롤 간격
            }
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, delay)
            }
        })
        handler.post(runnable)
    }

    override fun onResume() {
        super.onResume()
        searchEditTextView.setText("")
    }
}
