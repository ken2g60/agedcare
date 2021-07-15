package com.knightshell.agedcare

import android.app.Application
import io.sentry.android.core.SentryAndroid
import io.sentry.core.Sentry


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        VolleyService.initialize(this)
        SentryAndroid.init(applicationContext)
    }
}