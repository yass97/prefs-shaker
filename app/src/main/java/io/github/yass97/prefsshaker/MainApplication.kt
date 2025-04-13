package io.github.yass97.prefsshaker

import android.app.Application
import io.github.yass97.lib.PrefsShaker

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            PrefsShaker.init(this, getString(R.string.prefs_name))
        }
    }
}