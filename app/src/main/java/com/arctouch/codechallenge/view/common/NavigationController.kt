package com.arctouch.codechallenge.view.common

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.arctouch.codechallenge.MainActivity
import com.arctouch.codechallenge.R.id.container
import com.arctouch.codechallenge.view.details.DetailsFragment
import com.arctouch.codechallenge.view.home.HomeFragment
import javax.inject.Inject

class NavigationController @Inject constructor(activity: MainActivity) {

    private var containerId: Int = container
    private var fragmentManager: FragmentManager = activity.supportFragmentManager

    fun toHome() {
        val fragment = HomeFragment()
        replaceFragment(fragment)
    }

    fun toDetails(id: Int) {
        val fragment = DetailsFragment.newInstance(id)
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }
}
