package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.CodeChallengeApp

object AppInjector {
    fun init(codeChallengeApp: CodeChallengeApp) {
        DaggerAppComponent.builder()
                .application(codeChallengeApp)
                .build()
                .inject(codeChallengeApp)

        codeChallengeApp.registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
    }
}
