package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupIntroduceCreateBinding
import com.sansantek.sansanmulmul.ui.view.register.GroupCreateViewPagerFragment
import com.sansantek.sansanmulmul.ui.view.register.ViewPagerFragment
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel

private const val TAG = "GroupIntroduceCreateFragment_싸피"
class GroupIntroduceCreateFragment : BaseFragment<FragmentGroupIntroduceCreateBinding>(
    FragmentGroupIntroduceCreateBinding::bind,
    R.layout.fragment_group_introduce_create
) {
    private val viewPagerFragment by lazy {
        parentFragment as GroupCreateViewPagerFragment
    }
    private val viewModel : CreateGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.groupIntroduceBlank.doOnTextChanged{ text, start, before, cnt ->
            checkValid()
        }
        binding.groupCreateBlank.doOnTextChanged { text, start, before, cnt ->
            checkValid()
        }

        Log.d(TAG, "onViewCreated: parent $parentFragment")
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun checkValid(){
        if(binding.groupIntroduceBlank.text.isNullOrBlank() || binding.groupCreateBlank.text.isNullOrBlank()){
            viewPagerFragment.enableNextButton(false)
        }
        else{
            viewModel.apply {
                setGroupTitle(binding.groupCreateBlank.text.toString())
                setGroupDescription(binding.groupIntroduceBlank.text.toString())
            }
            viewPagerFragment.enableNextButton(true)
        }
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
        checkValid()
    }
}