package com.sansantek.sansanmulmul.ui.view.creategroup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupCreateFinishBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupCreateViewPagerBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.register.GroupCreateViewPagerFragment

class GroupCreateFinishFragment : BaseFragment<FragmentGroupCreateFinishBinding>(
    FragmentGroupCreateFinishBinding::bind,
    R.layout.fragment_group_create_finish
) {
    private val viewPagerFragment by lazy {
        parentFragment as GroupCreateViewPagerFragment
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener{
            val activity = requireActivity() as MainActivity
            activity.supportFragmentManager.popBackStack()
//            viewPagerFragment.parentFragmentManager.beginTransaction().remove(viewPagerFragment).commit()
        }
        // 텍스트뷰에 그라데이션 적용
        setGradient(binding.tvGroupCreateFinish)

        binding.btnCopyLink.setOnClickListener {
            val link = binding.tvGroupLink.text.toString()
            copyToClipboard(link)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("group link", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun setGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val textShader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                getColor(requireActivity(), R.color.gradientStartColor),
                getColor(requireActivity(), R.color.gradientEndColor)
            ), null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }
}
