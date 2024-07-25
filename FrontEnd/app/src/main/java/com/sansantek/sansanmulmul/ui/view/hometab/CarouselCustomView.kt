package com.sansantek.sansanmulmul.ui.view.hometab

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansantek.sansanmulmul.R

class CarouselCustomView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    lateinit var ivNewsImg: ImageView
    lateinit var tvNewsTitle: TextView

    init {
        init()
        getAttrs(attrs)
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.item_news, this, false)
        addView(view)
        ivNewsImg = findViewById(R.id.iv_news_img)
        tvNewsTitle = findViewById(R.id.tv_news_title)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarouselCustomView)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray) {
        ivNewsImg.setImageResource(
            typedArray.getResourceId(
                R.styleable.CarouselCustomView_newsImg,
                R.drawable.dummy1
            )
        )
        tvNewsTitle.text = typedArray.getText(R.styleable.CarouselCustomView_newsTitle)
        typedArray.recycle()
    }

}