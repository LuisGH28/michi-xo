package com.luigidev.michixo.mobile.data

import android.content.Context

const val CURRENT_APP_VERSION = 4
const val LATEST_AVAILABLE_VERSION = 5

interface UpdateVersionChecker {
    fun isUpdateAvailable(): Boolean
}

object MockUpdateVersionChecker : UpdateVersionChecker {
    override fun isUpdateAvailable(): Boolean {
        return LATEST_AVAILABLE_VERSION > CURRENT_APP_VERSION
    }
}

class UpdatePreferences(
    context: Context,
    private val versionChecker: UpdateVersionChecker = MockUpdateVersionChecker
) {
    private val prefs = context.getSharedPreferences(
        "michixo_update_preferences",
        Context.MODE_PRIVATE
    )

    fun shouldShowUpdateDialog(nowMillis: Long = System.currentTimeMillis()): Boolean {
        if (!versionChecker.isUpdateAvailable()) {
            return false
        }

        val dismissedAt = prefs.getLong(KEY_DISMISSED_AT, 0L)
        return dismissedAt == 0L || nowMillis - dismissedAt >= DISMISSAL_COOLDOWN_MILLIS
    }

    fun saveDismissalTimestamp(nowMillis: Long = System.currentTimeMillis()) {
        prefs.edit()
            .putLong(KEY_DISMISSED_AT, nowMillis)
            .apply()
    }

    companion object {
        private const val KEY_DISMISSED_AT = "update_dismissed_at"
        private const val DISMISSAL_COOLDOWN_MILLIS = 24L * 60L * 60L * 1000L
    }
}
