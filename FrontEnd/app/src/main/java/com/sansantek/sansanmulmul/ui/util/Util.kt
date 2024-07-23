package com.sansantek.sansanmulmul.ui.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object Util {
        val Float.dp: Int
            get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    fun Float.dpToPx(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
        )
    }
}