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
    private lateinit var newsList : List<News>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        // 뉴스 데이터를 가져온 후 ViewPager를 초기화합니다.
        setNewsData {
            initNewsViewPager(binding.layoutCarouselNews, it)
        }
        initRecommendationViewPager(binding.vpRecommendation1, 3000)
        initRecommendationViewPager(binding.vpRecommendation2, 3500)
        initRecommendationViewPager(binding.vpRecommendation3, 4000)
        initRecommendationViewPager(binding.vpRecommendation4, 4000)
    }

    private fun init() {
        searchEditTextView = binding.includeEditText.etSearch

        loadUserProfile()

        // 검색 완료 시 프래그먼트 이동
        searchEditTextView.setOnEditorActionListener(OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val mountainSearchResultFragment = MountainSearchResultFragment()
                val searchKeyword = searchEditTextView.text.toString()
                searchViewModel.setSearchKeyword(searchKeyword)

                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_view, mountainSearchResultFragment).commit()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun loadUserProfile(){
        sharedPreferencesUtil.getKakaoLoginToken()?.let {
            lifecycleScope.launch {
                val user = userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
                if(user.code() == 200){
                    user.body()?.let {
                        binding.tvShared.text = it.userNickName
                    }
                }
                else{
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

    private fun initRecommendationViewPager(viewPager: ViewPager2, autoScrollDelay: Long) {
        val itemList = setRecommendationData()
        val adapter = FirstRecommendationViewPagerAdapter(
            itemList,
            object : FirstRecommendationViewPagerAdapter.OnItemClickListener {
                override fun onItemClick(item: Recommendation) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_view, MountainDetailFragment()).commit()
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
    }

    private fun setNewsData(onNewsDataReady: (List<News>) -> Unit) {
        lifecycleScope.launch {
            newsList = newsService.getNewsKeyword("가리산")
            Log.d(TAG, "setNewsData: ${newsList}")
            onNewsDataReady(newsList)
        }
    }

    private fun setRecommendationData(): List<Recommendation> {
        return listOf(
            Recommendation("지리산", "어려움", R.drawable.dummy1),
            Recommendation("가야산", "보통", R.drawable.dummy2),
            Recommendation("가리왕산", "쉬움", R.drawable.dummy3),
        )
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
