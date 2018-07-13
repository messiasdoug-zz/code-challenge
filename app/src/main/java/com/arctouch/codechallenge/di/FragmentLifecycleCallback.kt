package com.arctouch.codechallenge.di

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import dagger.android.support.AndroidSupportInjection

class FragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentCreated(fm: FragmentManager?, fragment: Fragment?, savedInstanceState: Bundle?) {
        if (fragment is Injectable) {
            AndroidSupportInjection.inject(fragment)
        }
    }
}
