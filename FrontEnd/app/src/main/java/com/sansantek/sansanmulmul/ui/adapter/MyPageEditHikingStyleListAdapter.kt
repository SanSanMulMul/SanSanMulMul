package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.HikingStyle
import com.sansantek.sansanmulmul.databinding.ListMyPageEditHikingStyleBinding
import com.sansantek.sansanmulmul.databinding.ListMyPageHikingStyleBinding

class MyPageEditHikingStyleListAdapter():
    ListAdapter<HikingStyle, MyPageEditHikingStyleListAdapter.MyPageEditHikingStyleListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<HikingStyle>() {
        override fun areItemsTheSame(oldItem: HikingStyle, newItem: HikingStyle): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: HikingStyle, newItem: HikingStyle): Boolean {
            return oldItem == newItem
        }
    }

    inner class MyPageEditHikingStyleListHolder(private val binding: ListMyPageEditHikingStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            binding.tvHikingStyle.text = item.style
            when(position){
                0 -> {
                    if(item.check){
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_first_selected)
                    }
                    else{
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_first_unselected)
                    }
                    binding.tvHikingStyle.setPadding(10,30,10,30)
                }
                1 -> {
                    if(item.check){
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_second_selected)
                    }
                    else{
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_second_unselected)
                    }
                    binding.tvHikingStyle.setPadding(10,30,10,30)

                }
                2 -> {
                    if(item.check){
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_third_selected)
                    }
                    else{
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_third_unselected)
                    }
                    binding.tvHikingStyle.setPadding(10,30,10,30)
                }
                3 -> {
                    if(item.check){
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_fourth_selected)
                    }
                    else{
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_fourth_unselected)
                    }
                    binding.tvHikingStyle.setPadding(10,30,10,30)
                }
                4 -> {
                    if(item.check){
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_fifth_selected)
                    }
                    else{
                        setBackground(R.drawable.my_page_edit_hiking_style_round_stroke_fifth_unselected)
                    }
                    binding.tvHikingStyle.setPadding(10,30,10,30)
                }
                else -> {

                }
            }
            binding.root.setOnClickListener {
                itemClickListener.onClick(position)
            }
        }

        private fun setBackground(backgroundId: Int){
            binding.tvHikingStyle.background = ContextCompat.getDrawable(binding.root.context, backgroundId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageEditHikingStyleListHolder {
        return MyPageEditHikingStyleListHolder(
            ListMyPageEditHikingStyleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPageEditHikingStyleListHolder, position: Int) {
        holder.bindInfo(position)
    }

    interface ItemClickListener {
        fun onClick(position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}