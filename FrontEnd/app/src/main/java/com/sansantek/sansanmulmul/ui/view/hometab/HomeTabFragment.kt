package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
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
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainSearchViewModel
import kotlinx.coroutines.launch
import java.time.Month
import java.util.Calendar
import kotlin.math.abs
import kotlin.random.Random

private const val TAG = "HomeTabFragment 싸피"

class HomeTabFragment : BaseFragment<FragmentHomeTabBinding>(
    FragmentHomeTabBinding::bind,
    R.layout.fragment_home_tab
) {
    private lateinit var searchEditTextView: EditText
    private val searchViewModel: MountainSearchViewModel by activityViewModels()
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private var newsList =  mutableListOf<News>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        // 뉴스 데이터를 가져온 후 ViewPager를 초기화합니다.
        lifecycleScope.launch {
            setNewsData()
            initNewsViewPager(binding.layoutCarouselNews)
        }
        loadSeasonalRecommendations()
    }

    private fun init() {
        searchEditTextView = binding.includeEditText.etSearch

        setGreeting()
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

    private fun setGreeting() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        val timePeriod = when (currentHour) {
            in 5..8 -> "좋은 아침이에요,"
            in 9..11 -> "활기찬 오전 보내세요,"
            in 12..17 -> "오늘 오후도 화이팅!"
            in 18..20 -> "남은 하루도 화이팅~!"
            else -> "편안한 밤 되세요,"
        }

        binding.tvGreeting.text = timePeriod
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

    private fun initNewsViewPager(viewPager: ViewPager2) {
        val adapter = NewsViewPagerAdapter().apply { submitList(newsList) }
        viewPager.adapter = adapter

        viewPager.offscreenPageLimit = 1
        viewPager.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val itemDecoration = HorizontalMarginItemDecoration(horizontalMarginInDp = 20)
        viewPager.addItemDecoration(itemDecoration)
        viewPager.setPageTransformer { page, position ->
            val offsetX = position * -(2 * 120) // offset 값으로 간격 조정
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

    private suspend fun setNewsData() {
            mainActivityViewModel.token?.let {
                val response = mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
                if(response.isSuccessful){
                    if(response.body() == null || response.body()!!.isEmpty()){
                        newsList = newsService.getRandomNewsKeyword()
                    }else{
                        newsList = mutableListOf()
                        Log.d(TAG, "setNewsData: 사이즈 : ${response.body()!!.size}")
                        for(i in 0..4){
                            response.body()?.let { mountainList->
                                val size = mountainList.size
                                var randomIntInRange = Random.nextInt(0, size)
                                val mountainName = mountainList[randomIntInRange].mountainName
                                val articleList = newsService.getNewsKeyword(mountainName)
                                randomIntInRange = Random.nextInt(0, articleList.size)
                                newsList.add(articleList[randomIntInRange])
                            }
                        }
                    }
                }
                Log.d(TAG, "setNewsData: ${newsList}")
            }
    }

    private fun loadSeasonalRecommendations() {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val season = when (month) {
            3,4,5 -> "spring"
            6,7,8 -> "summer"
            9,10,11 -> "fall"
            else -> "winter"
        }
        val seasons = mapOf(
            "spring" to listOf(0, 1, 2, 3),
            "summer" to listOf(1, 2, 3, 0),
            "fall" to listOf(2, 3, 0, 1),
            "winter" to listOf(3, 0, 1, 2)
        )
        val seasonList = listOf("spring", "summer", "fall", "winter")
        val bindingList = listOf(binding.vpRecommendation1, binding.vpRecommendation2, binding.vpRecommendation3,binding.vpRecommendation4)
        val titleList = listOf(binding.tvRecommendation1, binding.tvRecommendation2, binding.tvRecommendation3, binding.tvRecommendation4)
        val seasonIndex = seasons[season]
        for(i in 0..3){
            lifecycleScope.launch {
                loadRecommendationData(bindingList[i], titleList[i], seasonList[seasonIndex!![i]], 5000)
            }
        }
    }

    private suspend fun loadRecommendationData(
        viewPager: ViewPager2,
        title: TextView,
        season: String,
        autoScrollDelay: Long
    ) {
        val seasonTitle = mapOf("spring" to "🌼 봄에 가기 좋은 산 🌼", "summer" to "🌊 여름에 가기 좋은 산 🌊", "fall" to "🍁 가을에 가기 좋은 산 🍁", "winter" to "❄ 겨울에 가기 좋은 산 ❄")
        title.text = seasonTitle[season]
        val recommendationList = when (season) {
            "spring" -> {
                mountainService.getMountainSpring()
            }
            "summer" -> {
                mountainService.getMountainSummer()
            }
            "fall" -> {
                mountainService.getMountainFall()
            }
            "winter" -> {
                mountainService.getMountainWinter()
            }
            else -> emptyList()
        }.map {
            Recommendation(it.mountainId, it.mountainName, it.mountainHeight, it.mountainImg)
        }

        // 계절별 데이터를 로그로 출력
        Log.d(TAG, "Season - $season, Data - $recommendationList")

        initRecommendationViewPager(viewPager, recommendationList, autoScrollDelay)
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
