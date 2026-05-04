package com.luigidev.michixo.mobile.data

import android.content.Context

class NotificationPreferences(
    context: Context
) {
    private val prefs = context.getSharedPreferences(
        "michixo_notification_preferences",
        Context.MODE_PRIVATE
    )

    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
            .apply()
    }

    fun wasPermissionAsked(): Boolean {
        return prefs.getBoolean(KEY_PERMISSION_ASKED, false)
    }

    fun setPermissionAsked() {
        prefs.edit()
            .putBoolean(KEY_PERMISSION_ASKED, true)
            .apply()
    }

    companion object {
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_PERMISSION_ASKED = "permission_asked"
    }
}