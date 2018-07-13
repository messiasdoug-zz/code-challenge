package com.arctouch.codechallenge.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.AndroidInjection
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector

class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (isActivityInjector(activity)) {
            AndroidInjection.inject(activity)
        }

        if (isFragmentInjector(activity)) {
            (activity as FragmentActivity).supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                            FragmentLifecycleCallback(), true)
        }
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    private fun isActivityInjector(activity: Activity?): Boolean = activity is HasSupportFragmentInjector || activity is HasActivityInjector

    private fun isFragmentInjector(activity: Activity?): Boolean = activity is FragmentActivity
}
