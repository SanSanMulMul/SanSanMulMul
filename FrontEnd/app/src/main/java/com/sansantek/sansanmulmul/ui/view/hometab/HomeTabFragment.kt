package com.sansantek.sansanmulmul.ui.view.hometab

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.News
import com.sansantek.sansanmulmul.data.model.Recommendation
import com.sansantek.sansanmulmul.databinding.FragmentHomeTabBinding
import com.sansantek.sansanmulmul.ui.adapter.FirstRecommendationViewPagerAdapter
import com.sansantek.sansanmulmul.ui.adapter.NewsViewPagerAdapter
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import kotlin.math.abs


class HomeTabFragment : BaseFragment<FragmentHomeTabBinding>(
  FragmentHomeTabBinding::bind,
  R.layout.fragment_home_tab
) {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()

    initNewsViewPager(binding.layoutCarouselNews)
    initRecommendationViewPager(binding.vpRecommendation1, 3000)
    initRecommendationViewPager(binding.vpRecommendation2, 3500)
    initRecommendationViewPager(binding.vpRecommendation3, 4000)
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
  }

  private fun initNewsViewPager(viewPager: ViewPager2) {
    val itemList = setNewsData()
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
    autoScroll(viewPager, 3000)
  }

  private fun initRecommendationViewPager(viewPager: ViewPager2, autoScrollDelay: Long) {
    val itemList = setRecommendationData()
    val adapter = FirstRecommendationViewPagerAdapter(
      itemList,
      object : FirstRecommendationViewPagerAdapter.OnItemClickListener {
        override fun onItemClick(item: Recommendation) {
//          Toast.makeText(context, "clicked : ${item.mountainName}", Toast.LENGTH_SHORT).show()
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

    // 자동 스크롤
    // autoScroll(viewPager, autoScrollDelay)
  }

  private fun setNewsData(): List<News> {
    return listOf(
      News("뉴스1", R.drawable.dummy1),
      News("뉴스2", R.drawable.dummy2),
      News("뉴스3", R.drawable.dummy3)
    )
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
}