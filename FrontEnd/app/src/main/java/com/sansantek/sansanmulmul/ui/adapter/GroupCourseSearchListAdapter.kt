package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.ListSearchMountainBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class CourseItem(
    val name: String,
    var isFavorite: Boolean = false // Default to not favorite
)

private const val TAG = "GroupCourceSearchListAd 싸피"

class GroupCourceSearchListAdapter(
) : ListAdapter<Mountain, GroupCourceSearchListAdapter.GroupCourseSearchListHolder>(Comparator) {
    private val token = sharedPreferencesUtil.getKakaoLoginToken()

    companion object Comparator : DiffUtil.ItemCallback<Mountain>() {
        override fun areItemsTheSame(oldItem: Mountain, newItem: Mountain): Boolean {
            return oldItem.mountainId == newItem.mountainId
        }

        override fun areContentsTheSame(oldItem: Mountain, newItem: Mountain): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupCourseSearchListHolder(private val binding: ListSearchMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var check = false
        fun bindInfo(position: Int) {
            val item = getItem(position)
            Log.d(TAG, "bindInfo: ghcnf")
            runBlocking {
                launch {
                    val result = mountainService.getMountainCourse(item.mountainId)
                    if (result.isSuccessful) {
                        result.body()?.let {
                            binding.groupCourseNumberText.text = "코스 총 ${it.courseCount}개"
                        }
                    }
                }
                // 즐겨찾기 로직
                launch {
                    token?.let {
                        val result =
                            mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
                        if (result.isSuccessful) {
                            result.body()?.let { ret ->
                                if (ret.any { search -> search.mountainId == item.mountainId }) {
                                    check = true
                                    binding.btnFavoriteMountain.setImageResource(R.drawable.star_filled_favorite)
                                } else {
                                    check = false
                                    binding.btnFavoriteMountain.setImageResource(R.drawable.star_empty_favorite)
                                }
                                Log.d(TAG, "bindInfo: $check")
                            }
                        }
                    }
                }
            }

            Glide.with(binding.root).load(item.mountainImg).into(binding.mountainImage)
            binding.mountainTitle.text = item.mountainName
            binding.mountainImage.setOnClickListener {
                itemClickListener.onMountainClick(item)
            }
            binding.layoutMountainSearchInfo.setOnClickListener {
                itemClickListener.onMountainClick(item)
            }
            // 버튼 클릭 시 즐겨찾기 상태 토글
            binding.btnFavoriteMountain.setOnClickListener {
                check = !check // Toggle the favorite state
                // 새로운 상태에 따라 버튼 이미지 업데이트
                binding.btnFavoriteMountain.setImageResource(
                    if (check) R.drawable.star_filled_favorite
                    else R.drawable.star_empty_favorite
                )
                itemClickListener.onLikeClick(item, check)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupCourseSearchListHolder {
        return GroupCourseSearchListHolder(
            ListSearchMountainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupCourseSearchListHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${getItem(position)}")
        // 현재 위치의 항목을 가져와서 뷰 홀더에 바인딩
        holder.bindInfo(position) // 바인딩 메서드 호출
    }

    interface ItemClickListener {
        fun onMountainClick(mountain: Mountain)
        fun onLikeClick(mountain: Mountain, check: Boolean)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}