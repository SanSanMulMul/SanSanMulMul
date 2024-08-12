package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainWithCourseCnt
import com.sansantek.sansanmulmul.databinding.ItemSearchResultOfMountainBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import kotlinx.coroutines.runBlocking

private const val TAG = "SearchResultOfMountainListAdapter_싸피"

class SearchResultOfMountainListAdapter :
    ListAdapter<MountainWithCourseCnt, SearchResultOfMountainListAdapter.MountainViewHolder>(
        Comparator
    ) {
    private val token = sharedPreferencesUtil.getKakaoLoginToken()

    companion object Comparator : DiffUtil.ItemCallback<MountainWithCourseCnt>() {
        override fun areItemsTheSame(
            oldItem: MountainWithCourseCnt,
            newItem: MountainWithCourseCnt
        ): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(
            oldItem: MountainWithCourseCnt,
            newItem: MountainWithCourseCnt
        ): Boolean {
            return oldItem == newItem
        }
    }

    private var lastPosition = -1
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(mountain: Mountain)
        fun onLikeClick(mountain: Mountain, check: Boolean)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    inner class MountainViewHolder(private val binding: ItemSearchResultOfMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var check = false
        fun bindInfo(position: Int) {
            val item = getItem(position)
            val mountain = item.mountain
            if (mountain.mountainImg == null) {
                // 없을 경우 기본 이미지. 글라이드 : 링크이미지 받아올 때
                Glide.with(binding.root)
                    .load("https://images-ext-1.discordapp.net/external/9pyEBG4x_J2aG-j5BeoaA8edEpEpfQEOEO9SdmT9hIg/https/k.kakaocdn.net/dn/cwObI9/btsGqPcg5ic/UHYbwvy2M2154EdZSpK8B1/img_110x110.jpg%2C?format=webp")
                    .into(binding.ivMountainImg)
            } else {
                Glide.with(binding.root)
                    .load(mountain.mountainImg)
                    .into(binding.ivMountainImg)
            }
            runBlocking {
                // 즐겨찾기 로직
                token?.let {
                    val result =
                        mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
                    if (result.isSuccessful) {
                        result.body()?.let { ret ->
                            if (ret.any { search -> search.mountainId == mountain.mountainId }) {
                                check = true
                                binding.ibFavoriteBtn.setImageResource(R.drawable.star_filled_favorite)
                            } else {
                                check = false
                                binding.ibFavoriteBtn.setImageResource(R.drawable.star_empty_favorite)
                            }
                            Log.d(TAG, "bindInfo: $check")
                        }
                    }
                }

            }
            binding.tvMountainName.text = mountain.mountainName
            binding.tvCourseCnt.text = "코스 총 ${item.courseCount}개"

            binding.root.setOnClickListener { itemClickListener.onItemClick(mountain) }
            binding.ibFavoriteBtn.setOnClickListener {
                check = !check // Toggle the favorite state
                // 새로운 상태에 따라 버튼 이미지 업데이트
                binding.ibFavoriteBtn.setImageResource(
                    if (check) R.drawable.star_filled_favorite
                    else R.drawable.star_empty_favorite
                )
                itemClickListener.onItemClick(mountain)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MountainViewHolder {
        return MountainViewHolder(
            ItemSearchResultOfMountainBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bindInfo(position)

        // 애니메이션 설정
        setAnimation(holder.itemView, position)
    }


    private fun setAnimation(viewToAnimate: View, position: Int) {
        // 애니메이션 딜레이 설정
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_from_right)
            animation.startOffset = (position * 100).toLong()
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}