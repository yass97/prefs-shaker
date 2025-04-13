package io.github.yass97.lib

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import java.io.File

object PrefsShaker {
    private var isInitialized = false

    fun init(application: Application, prefsName: String) {
        if (!isDebug(application)) {
            Log.d(javaClass.simpleName, "not debug build.")
            return
        }

        if (!existsPrefs(application, prefsName)) {
            Log.d(javaClass.simpleName, "prefs file not found.")
            return
        }

        if (isInitialized) return

        isInitialized = true

        ShakeDetector(application) {
            val intent = PrefsViewerActivity.createIntent(application, prefsName)
            application.startActivity(intent)
        }.also {
            it.start()
        }
    }

    private fun isDebug(context: Context): Boolean {
        return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    private fun existsPrefs(context: Context, prefsName: String): Boolean {
        val file = File(context.applicationInfo.dataDir, "shared_prefs/$prefsName.xml")
        return file.exists()
    }
}