package com.arctouch.codechallenge.view.common

import android.support.v4.app.FragmentManager
import com.arctouch.codechallenge.MainActivity
import com.arctouch.codechallenge.R.id.container
import com.arctouch.codechallenge.view.home.HomeFragment
import javax.inject.Inject

class NavigationController @Inject constructor(activity: MainActivity) {

    private var containerId: Int = container
    private var fragmentManager: FragmentManager = activity.supportFragmentManager

    fun toHome() {
        val fragment = HomeFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

}
