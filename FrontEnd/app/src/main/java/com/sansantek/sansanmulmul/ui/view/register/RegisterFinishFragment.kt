package com.sansantek.sansanmulmul.ui.view.register

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterFinishBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.LoginActivityViewModel


class RegisterFinishFragment : BaseFragment<FragmentRegisterFinishBinding>(
    FragmentRegisterFinishBinding::bind,
    R.layout.fragment_register_finish
) {
    private val activityViewModel: LoginActivityViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvShared.text = activityViewModel.user.kakaoAccount?.profile?.nickname
        val anim = AnimationUtils.loadAnimation(requireActivity(), R.anim.blink)
        binding.homeButton.startAnimation(anim)

        binding.homeButton.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val options = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                binding.tvShared,
                "shared_element_text"
            )

            startActivity(intent, options.toBundle())
            activity?.finish()
        }


    }

}