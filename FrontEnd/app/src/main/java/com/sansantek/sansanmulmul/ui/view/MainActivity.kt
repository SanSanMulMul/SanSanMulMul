package com.sansantek.sansanmulmul.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.databinding.ActivityMainBinding
import com.sansantek.sansanmulmul.ui.view.grouptab.GroupTabFragment
import com.sansantek.sansanmulmul.ui.view.hometab.HomeTabFragment
import com.sansantek.sansanmulmul.ui.view.maptab.MapTabFragment

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    initBottomNav()
    changeFragment(HomeTabFragment())

  }


  private fun initBottomNav() {

    binding.mainLayoutBottomNavigation.setOnItemSelectedListener {
//            Log.d(TAG, "initBottomNav: ${it.itemId}")
      when (it.itemId) {
        R.id.home -> {
          changeFragment(HomeTabFragment())
        }

        R.id.map -> {
          changeFragment(MapTabFragment())
        }

        R.id.mountain -> {

        }

        R.id.group -> {
          changeFragment(GroupTabFragment())
        }

        R.id.mypage -> {

        }

      }
      return@setOnItemSelectedListener true
    }
  }

  fun changeFragment(view: Fragment) {
    supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, view).commit()
  }

  fun changeAddToBackstackFragment(view: Fragment) {
    supportFragmentManager.beginTransaction().replace(binding.fragmentView.id, view)
      .addToBackStack(null).commit()
  }

}