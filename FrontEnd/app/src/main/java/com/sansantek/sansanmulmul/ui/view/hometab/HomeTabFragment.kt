package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Recommendation
import com.sansantek.sansanmulmul.databinding.FragmentHomeTabBinding
import com.sansantek.sansanmulmul.ui.adapter.FirstRecommendationViewPagerAdapter


class HomeTabFragment : BaseFragment<FragmentHomeTabBinding>(
    FragmentHomeTabBinding::bind,
    R.layout.fragment_home_tab
) {
    private var selectedIndex: Int = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initCarousel()
        initViewPager(binding.vpRecommendation1, 3000)
        initViewPager(binding.vpRecommendation2, 3500)
        initViewPager(binding.vpRecommendation3, 4000)
    }

    private fun initCarousel() {
        val motionLayout = binding.layoutCarouselNews
        val v1 = binding.root.findViewById<View>(R.id.v1)
        val v2 = binding.root.findViewById<View>(R.id.v2)
        val v3 = binding.root.findViewById<View>(R.id.v3)

        v1.setOnClickListener {
            if (selectedIndex == 0) return@setOnClickListener

            motionLayout.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            motionLayout.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) return@setOnClickListener

            if (selectedIndex == 2) {
                motionLayout.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                motionLayout.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) return@setOnClickListener

            motionLayout.setTransition(R.id.s2, R.id.s3) //orange to red transition
            motionLayout.transitionToEnd()
            selectedIndex = 2;
        }

//        // 자동 슬라이드 설정
//        var currentSlide = 0
//        val totalSlides = 3 // 슬라이드 개수
//        val handler = Handler(Looper.getMainLooper())
//        val runnable = object : Runnable {
//            override fun run() {
//                currentSlide = (currentSlide + 1) % totalSlides
//                motionLayout.transitionToState(if (currentSlide == 0) R.id.s else R.id.end)
//                handler.postDelayed(this, 3000) // 3초마다 슬라이드
//            }
//        }
//        handler.postDelayed(runnable, 3000)
    }

    private fun initViewPager(viewPager: ViewPager2, autoScrollDelay: Long) {
        val itemList = setData()
        val adapter = FirstRecommendationViewPagerAdapter(itemList)
        viewPager.adapter = adapter

        // 중간 아이템부터 보이도록
        val middlePosition = Int.MAX_VALUE / 2
        val initialPosition = middlePosition - middlePosition % adapter.items.size
        viewPager.setCurrentItem(initialPosition, false)

        // 양쪽 보이게
        viewPager.offscreenPageLimit = 3
        val itemDecoration = HorizontalMarginItemDecoration(horizontalMarginInDp = 20)
        viewPager.addItemDecoration(itemDecoration)
        viewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * 180) // offset 값으로 간격 조정
            page.translationX = offset
        }

        // 자동 스크롤
        // autoScroll(viewPager, autoScrollDelay)
    }

    private fun setData(): List<Recommendation> {
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
}