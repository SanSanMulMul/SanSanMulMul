package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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
  private var currentPage = 0
  private val handler = Handler(Looper.getMainLooper())

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initCarousel()
    initViewPager()
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

    // 자동 슬라이드 설정
    var currentSlide = 0
    val totalSlides = 3 // 슬라이드 개수
    val handler = Handler(Looper.getMainLooper())
    val runnable = object : Runnable {
      override fun run() {
        currentSlide = (currentSlide + 1) % totalSlides
        motionLayout.transitionToState(if (currentSlide == 0) R.id.start else R.id.end)
        handler.postDelayed(this, 3000) // 3초마다 슬라이드
      }
    }
    handler.postDelayed(runnable, 3000)
  }

  private fun initViewPager() {
    val viewPager: ViewPager2 = binding.vpRecommendation1
    val mountainItems = listOf(
      Recommendation("지리산", "어려움", R.drawable.dummy1),
      Recommendation("가야산", "보통", R.drawable.dummy1),
      Recommendation("가리왕산", "쉬움", R.drawable.dummy1),
    )
    viewPager.adapter = FirstRecommendationViewPagerAdapter(mountainItems)
    viewPager.offscreenPageLimit = 3

    val itemDecoration =
      HorizontalMarginItemDecoration(horizontalMarginInDp = 20)
    viewPager.addItemDecoration(itemDecoration)

    // 양쪽 보이게
    viewPager.setPageTransformer { page, position ->
      val offset = position * -(2 * 120) // offset 값으로 간격조정
      page.translationX = offset
    }

  }
}