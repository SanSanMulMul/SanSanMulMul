package com.sansantek.sansanmulmul.config

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sansantek.sansanmulmul.R

private const val TAG = "BaseFragment 싸피"
// Fragment의 기본을 작성, 뷰 바인딩 활용
abstract class BaseFragment<B : ViewBinding>(
  private val bind: (View) -> B,
  @LayoutRes layoutResId: Int
) : Fragment(layoutResId) {
  private var _binding: B? = null
  protected val binding get() = _binding!!
  lateinit var myContext: Context

  override fun onAttach(context: Context) {
    super.onAttach(context)
    myContext = context
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = bind(super.onCreateView(inflater, container, savedInstanceState)!!)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  fun showToast(message: String) {
    Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show()
  }

  fun hideBottomNav(bottomNavigationView: BottomNavigationView, state: Boolean) {
    if (state) bottomNavigationView.visibility = View.GONE else bottomNavigationView.visibility =
      View.VISIBLE
  }


  fun changeFragmentWithPopUpAnimation(toFragment: Fragment) {
    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
    fragmentTransaction.setCustomAnimations(
      R.anim.slide_in_up,  // 프래그먼트가 나타날 때
      0, // 프래그먼트가 사라질 때
      R.anim.fade_in_faster,  // 백스택에서 돌아올 때 나타날 때
      R.anim.slide_out_down // 백스택에서 돌아올 때 사라질 때
    )
    fragmentTransaction.addToBackStack(null)
      .replace(R.id.fragment_view, toFragment).commit()
  }

  fun changeFragmentWithFadeInOutAnimation(toFragment: Fragment) {
    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
    fragmentTransaction.setCustomAnimations(
      R.anim.fade_in_faster,
      R.anim.fade_out_faster,
      R.anim.fade_in_faster,
      R.anim.fade_out_faster
    )
    fragmentTransaction.addToBackStack(null)
      .replace(R.id.fragment_view, toFragment).commit()
  }

  fun changeFragmentWithSlideRightAnimation(toFragment: Fragment) {
    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
    fragmentTransaction.setCustomAnimations(
      R.anim.slide_in_from_right,
      0,
      R.anim.fade_in_faster,
      R.anim.slide_out_to_right
    )
    fragmentTransaction.addToBackStack(null)
      .replace(R.id.fragment_view, toFragment).commit()
  }

  protected fun safeCall(action: () -> Unit) {
    if (_binding != null) {
      action()
    }else{
      Log.d(TAG, "safeCall: binding이 null")
    }
  }
}