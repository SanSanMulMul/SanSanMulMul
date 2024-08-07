package com.sansantek.sansanmulmul.ui.view.register

import android.graphics.LinearGradient
import android.graphics.Shader
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterExtraInfoBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.viewmodel.LoginActivityViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "RegisterExtraInfoFragme 싸피"

class RegisterExtraInfoFragment : BaseFragment<FragmentRegisterExtraInfoBinding>(
    FragmentRegisterExtraInfoBinding::bind,
    R.layout.fragment_register_extra_info
) {
    private val viewPagerFragment by lazy {
        parentFragment as ViewPagerFragment
    }
    private val activityViewModel: LoginActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.etNickname.setOnKeyListener{ v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                activityViewModel.setUserNickName(binding.etNickname.text.toString())
                true
            }
            false
        }
        binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "onViewCreated: $checkedId")
            if(checkedId == binding.rbMale.id) {
                activityViewModel.setUserGender("M")
            }
            else{
                activityViewModel.setUserGender("F")
            }
        }
        binding.npYear.setOnValueChangedListener { picker, oldValue, newValue ->
            activityViewModel.setUserBirth("${DecimalFormat("00").format(newValue)}-${DecimalFormat("00").format(binding.npMonth.value)}-${DecimalFormat("00").format(binding.npDay.value)}")
        }
        binding.npMonth.setOnValueChangedListener { picker, oldValue, newValue ->
            activityViewModel.setUserBirth("${DecimalFormat("00").format(binding.npYear.value)}-${DecimalFormat("00").format(newValue)}-${DecimalFormat("00").format(binding.npDay.value)}")
        }
        binding.npDay.setOnValueChangedListener { picker, oldValue, newValue ->
            activityViewModel.setUserBirth("${DecimalFormat("00").format(binding.npYear.value)}-${DecimalFormat("00").format(binding.npMonth.value)}-${DecimalFormat("00").format(newValue)}")
        }
    }

    private fun registerObserver(){
        activityViewModel.userGender.observe(viewLifecycleOwner){
            Log.d(TAG, "observeGender: 성별을 변경했어요! 유효성 검사를 다시 진행할게요!")
            checkValid()
        }
        activityViewModel.userNickname.observe(viewLifecycleOwner){
            Log.d(TAG, "registerObserver: ")
            checkValid()
        }
        activityViewModel.userName.observe(viewLifecycleOwner){
            Log.d(TAG, "registerObserver: ")
            checkValid()
        }
        activityViewModel.userBirth.observe(viewLifecycleOwner){
            checkValid()
        }
    }

    private fun checkValid(){
        lifecycleScope.launch {
            val isNickNameAvailable = async {
                userService.isAvailableNickName(binding.etNickname.text.toString()).let {
                    if (it.body() == null) false
                    else {
                        // null 체크해서 절대 null일리가 없음
                        it.body()!!
                    }
                }
            }
            val isGenderSelected = binding.rgGender.checkedRadioButtonId != -1
            Log.d(TAG, "checkValid: ${isNickNameAvailable}  $isGenderSelected")
            if(isNickNameAvailable.await() && isGenderSelected){
                viewPagerFragment.enableNextButton(true)
            }
            else{
                viewPagerFragment.enableNextButton(false)
            }
        }
    }

    private fun init(){
        viewPagerFragment.enableNextButton(false)
        registerObserver()
        loadUserNickName()
        setGradient(binding.extraInfoText1)
        setGradient(binding.extraInfoText2)
        setSpinner()
    }
    private fun loadUserNickName(){
        // 카카오에서 닉네임 입력받은 것으로 초기 세팅
        val user = activityViewModel.user
        user.kakaoAccount?.profile?.let{
            binding.etNickname.setText(it.nickname)
            activityViewModel.setUserName(it.nickname ?: "")
            activityViewModel.setUserNickName(it.nickname ?: "")
        }
    }
    private fun setGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val textShader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                getColor(requireActivity(), R.color.sansanmulmul_green),
                getColor(requireActivity(), R.color.gradientEndColor)
            ), arrayOf(0f, 1f).toFloatArray(), Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }

    private fun setSpinner() {
        val year: NumberPicker = binding.npYear
        val month: NumberPicker = binding.npMonth
        val day: NumberPicker = binding.npDay
        val currentDate = getCurrentDate()

        Log.d(
            TAG,
            "setSpinner: ${currentDate.year}, ${currentDate.monthValue}, ${currentDate.dayOfMonth}"
        )
        initNumberPicker(year, 1900, maxVal = currentDate.year)
        initNumberPicker(month, minVal = 1, maxVal = 12)
        initNumberPicker(day, minVal = 1, maxVal = 31)

        year.value = currentDate.year
        month.value = currentDate.monthValue
        day.value = currentDate.dayOfMonth

        year.setOnValueChangedListener { _, _, newVal ->
            day.maxValue = updateDayPicker(newVal, month.value)
        }
        month.setOnValueChangedListener { _, _, newVal ->
            day.maxValue = updateDayPicker(year.value, newVal)
        }
    }



    private fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }

    private fun initNumberPicker(
        numberPicker: NumberPicker,
        minVal: Int,
        maxVal: Int
    ) {
        with(numberPicker) {
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minValue = minVal
            maxValue = maxVal
        }
    }

    private fun updateDayPicker(year: Int, month: Int): Int {
        return when (month) {
            4, 6, 9, 11 -> 30
            2 -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
            else -> 31
        }
    }

}
