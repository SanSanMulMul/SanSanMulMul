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
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.SearchMountainListItem
import com.sansantek.sansanmulmul.databinding.ItemBottomSheetMountainBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val token = sharedPreferencesUtil.getKakaoLoginToken()

private const val TAG = "BottomSheetMountainList 싸피"
class BottomSheetMountainListAdapter() :
    ListAdapter<SearchMountainListItem, BottomSheetMountainListAdapter.MountainViewHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<SearchMountainListItem>() {
        override fun areItemsTheSame(oldItem: SearchMountainListItem, newItem: SearchMountainListItem): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: SearchMountainListItem, newItem: SearchMountainListItem): Boolean {
            return oldItem == newItem
        }
    }

    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun onItemClick(mountain: SearchMountainListItem)
        fun onLikeClick(mountain: SearchMountainListItem, check: Boolean)
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }



    inner class MountainViewHolder(private val binding: ItemBottomSheetMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var check = false
        fun bindInfo(position: Int) {
            val item = getItem(position)
            Glide.with(binding.root.context)
                .load(item.mountainImg)
                .into(binding.ivMountain)
            binding.tvMountainName.text = item.mountainName
            binding.tvMountainCourseCnt.text = "코스 총 " + item.courseCnt + "개"

            binding.layoutMountain.setOnClickListener { itemClickListener.onItemClick(item) }
            binding.ibFavoriteBtn.setOnClickListener {
                check = !check // Toggle the favorite state
                // 새로운 상태에 따라 버튼 이미지 업데이트
                binding.ibFavoriteBtn.setImageResource(
                    if (check) R.drawable.star_filled_favorite
                    else R.drawable.star_empty_favorite
                )
                itemClickListener.onLikeClick(item, check)
            }

            runBlocking {
            // 즐겨찾기 로직
                launch {
                    token?.let {
                        val result =
                            mountainService.getLikedMountainList(makeHeaderByAccessToken(it.accessToken))
                        if (result.isSuccessful) {
                            result.body()?.let { ret ->
                                if (ret.any { search -> search.mountainId == item.mountainId }) {
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
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MountainViewHolder {
        return MountainViewHolder(
            ItemBottomSheetMountainBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bindInfo(position)
    }
}