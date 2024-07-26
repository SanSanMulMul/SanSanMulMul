package com.sansantek.sansanmulmul.ui.adapter.itemdecoration

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "SpaceItemDecoration_싸피"
class SpaceItemDecoration(private val space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        /** 첫번째 열이 아닌(None Column-1) 아이템들만 좌측에 [space] 만큼의 여백을 추가한다. 즉, 첫번째 열에 있는 아이템에는 좌측에 여백을 주지 않는다. */
        Log.d(TAG, "getItemOffsets: $position")
        if (position != 0){
            outRect.left = space
        }

    }

}