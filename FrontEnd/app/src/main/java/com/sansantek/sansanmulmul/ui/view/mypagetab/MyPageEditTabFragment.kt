package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLE
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLESIZE
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.HikingStyle
import com.sansantek.sansanmulmul.data.model.ProfileUpdateData
import com.sansantek.sansanmulmul.databinding.FragmentMyPageEditBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageEditHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.GridSpaceItemDecoration
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.getRealPathFromURI
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private const val TAG = "MyPageEditTabFragment 싸피"

class MyPageEditTabFragment : BaseFragment<FragmentMyPageEditBinding>(
    FragmentMyPageEditBinding::bind,
    R.layout.fragment_my_page_edit
) {
    private val permissionList = arrayOf(Manifest.permission.CAMERA)
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var hikeStyleList =
        mutableListOf<HikingStyle>() //mutableListOf(HikingStyle(HIKINGSTYLE[], true), HikingStyle("등산도 식후경", false), HikingStyle("어쩌구", true), HikingStyle("저쩌구", false), HikingStyle("어쩌구저쩌구", false))
    private lateinit var hikeAdapter: MyPageEditHikingStyleListAdapter
    private lateinit var titleList: List<String>
    private lateinit var myPageAdapter: ArrayAdapter<String>
    private lateinit var permissionChecker: PermissionChecker
    private lateinit var activity: Context
    private lateinit var uri: Uri
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        permissionChecker = PermissionChecker(this)
        binding.ibEditButton.setOnClickListener {
            if (permissionChecker.checkPermission(activity, permissionList)) {
                permissionChecker.setOnGrantedListener { //퍼미션 획득 성공일때
                    openGallery()
                }
                permissionChecker.requestPermissionLauncher.launch(permissionList) // 권한없으면 창 띄움
            } else {
                openGallery()
            }
        }
        binding.root.setOnClickListener {
            hideKeyboard()
        }

        binding.etNickname.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
            }
            false
        }

        binding.spinnerTitle.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.let {
                    val item = it.getItemAtPosition(position)
                    Log.d(TAG, "onItemSelected: $item")
                    Log.d(TAG, "onItemSelected: ${TITLE.indexOf(item)}")
                    activityViewModel.setUserTitle(TITLE.indexOf(item))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.etNickname.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                // 포커스가 사라질 때 실행할 로직
                checkValidNickname()
            }
        }
        
        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                val styleList = hikeStyleList.filter {
                    it.check
                }.map { style ->
                    HIKINGSTYLE.indexOf(style.style)
                }
                Log.d(TAG, "onViewCreated: styleList $styleList")
                activityViewModel.token?.let {
                    val isValid = userService.chkDuplicateNickname(
                        makeHeaderByAccessToken(it.accessToken),
                        binding.etNickname.text.toString()
                    )
                    if (isValid.code() != 409) {
                        if (!::uri.isInitialized) {
                            uri = Uri.EMPTY
                        }
                        Log.d(TAG, "onViewCreated: uri정보 : $uri")
                        Log.d(
                            TAG, "onViewCreated: uri의 진짜 경로 ${
                                getRealPathFromURI(
                                    requireContext(),
                                    uri
                                )
                            }"
                        )
                        var result = getRealPathFromURI(requireContext(), uri).let { file ->
                            val body = if (file == null) {
                                MultipartBody.Part.createFormData(
                                    "image",
                                    "",
                                    RequestBody.create("image/*".toMediaTypeOrNull(), ByteArray(0))
                                )
                            } else {
                                val file = File(file)
                                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                                MultipartBody.Part.createFormData("image", file.name, requestBody)
                            }
                            Log.d(TAG, "onViewCreated: 여기도 실행")
                            userService.updateUserProfile(
                                makeHeaderByAccessToken(it.accessToken),
                                body,
                                ProfileUpdateData(
                                    binding.etNickname.text.toString(),
                                    activityViewModel.userTitle,
                                    styleList
                                )
                            )
                        }

                        lifecycleScope.launch {

                            val job1 = async {
                                val newUser =
                                    userService.loadUserProfile(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setUser(newUser.body()!!)
                            }
                            val job2 = async {
                                val newMyPage =
                                    userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setMyPageInfo(newMyPage)
                            }
                            val job3 = async {
                                val newHikingStyle =
                                    userService.getHikingStyle(makeHeaderByAccessToken(it.accessToken))
                                activityViewModel.setHikingStyles(newHikingStyle)
                            }
                            job1.await()
                            job2.await()
                            job3.await()
                            showToast("프로필 수정이 성공적으로 완료되었습니다!")
                            requireActivity().supportFragmentManager.popBackStack()
                        }

                    } else {
                        binding.textInputLayoutNickname.apply {
                            error = "이미 사용 중인 닉네임입니다!"
                        }
                    }
                }
            }
        }
        loadTitle()
        binding.apply {
            Glide.with(root)
                .load(activityViewModel.user.userProfileImg)
                .into(ivProfileImg)
            etNickname.setText(activityViewModel.user.userNickName)

        }
        Log.d(TAG, "onViewCreated: ${activityViewModel.hikingStyles.value}")
        for (i in 1..HIKINGSTYLESIZE) {
            activityViewModel.hikingStyles.value?.let {
                if (it.contains(i)) {
                    hikeStyleList.add(HikingStyle(HIKINGSTYLE[i], true))
                } else {
                    hikeStyleList.add(HikingStyle(HIKINGSTYLE[i], false))
                }
            }
        }

        hikeAdapter = MyPageEditHikingStyleListAdapter()
        binding.rvHikeStyle.apply {
            itemAnimator = null
            adapter = hikeAdapter.apply {
                submitList(hikeStyleList)
                setItemClickListener(object : MyPageEditHikingStyleListAdapter.ItemClickListener {
                    override fun onClick(position: Int) {
                        hikeStyleList = hikeStyleList.mapIndexed { idx, it ->
                            if (idx == position) {
                                HikingStyle(it.style, !it.check)
                            } else {
                                HikingStyle(it.style, it.check)
                            }
                        }.toMutableList()
                        submitList(hikeStyleList)
                    }
                })
            }
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridSpaceItemDecoration(2, 60))
        }

        super.onViewCreated(view, savedInstanceState)

    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK)
        gallery.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*"
        )
        pickImageLauncher.launch(gallery)
        Log.d(TAG, "openGallery: 사진 추가")
    }

    private fun checkValidNickname() {
        val inputText = binding.etNickname.text.toString()
        lifecycleScope.launch {
            activityViewModel.token?.let {
                Log.d(TAG, "onViewCreated: $inputText")
                val result = userService.chkDuplicateNickname(
                    makeHeaderByAccessToken(it.accessToken),
                    inputText
                )
                Log.d(TAG, "onViewCreated: $result")
                if (result.code() == 200) {
                    binding.textInputLayoutNickname.apply {
                        error = null
                    }
                } else {
                    binding.textInputLayoutNickname.apply {
                        error = "이미 사용 중인 닉네임입니다!"
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        Log.d(TAG, "hideKeyboard: 키보드 숨기기 실행")
        if (requireActivity().currentFocus != null) {
            if (binding.textInputLayoutNickname.hasFocus()) {
                val inputManager =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    requireActivity().currentFocus?.windowToken,
                    0
                )
                binding.textInputLayoutNickname.clearFocus()
            }
        }
    }

    private fun loadTitle() {
        activityViewModel.token?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                titleList = userService.getMyBadgeList(makeHeaderByAccessToken(it.accessToken))
                launch(Dispatchers.Main) {
                    myPageAdapter =
                        ArrayAdapter(requireContext(), R.layout.item_search_spinner, titleList)
                    binding.spinnerTitle.adapter = myPageAdapter
                    binding.spinnerTitle.setSelection(titleList.indexOf(TITLE[activityViewModel.userTitle]))
                }
            }
        }
    }

    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { it ->
                    Log.d(TAG, "수신 양호: $it")
                    val image = it
                    uri = it
                    binding.ivProfileImg.setImageURI(image)
                }
            }
        }

}