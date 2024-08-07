package com.sansantek.sansanmulmul.ui.adapter.layoutmanager

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager


class CustomLayoutmanager(context: Context?, private val columnCount: Int) :
  FlexboxLayoutManager(context) {
  override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
    super.onLayoutChildren(recycler, state)
    if (columnCount <= 0) {
      super.onLayoutChildren(recycler, state)
      return
    }

    detachAndScrapAttachedViews(recycler)

    val itemCount = itemCount
    if (itemCount == 0) {
      return
    }

    var childWidth: Int
    var childHeight: Int
    var leftOffset = 0
    var topOffset = 0
    var rowCount = 0

    for (i in 0 until itemCount) {
      val view = recycler.getViewForPosition(i)
      measureChildWithMargins(view, 0, 0)
      addView(view)
      childWidth = getDecoratedMeasuredWidth(view)
      childHeight = getDecoratedMeasuredHeight(view)
      val widthSpec = View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY)
      val heightSpec = View.MeasureSpec.makeMeasureSpec(childHeight, View.MeasureSpec.EXACTLY)
      view.measure(widthSpec, heightSpec)

      if (i % columnCount == 0 && i != 0) {
        leftOffset = 0
        topOffset += childHeight
        rowCount++
      }
      if (i % columnCount != 0) {
        leftOffset += 20
      }

      layoutDecorated(view, leftOffset, topOffset, leftOffset + childWidth, topOffset + childHeight)
      leftOffset += childWidth
    }
  }
}
