package com.luigidev.michixo.mobile.presentation.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageManager {

    fun setLanguage(languageTag: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageTag)
        )
    }

    fun resetToSystem() {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.getEmptyLocaleList()
        )
    }

    fun currentLanguageTag(): String {
        return AppCompatDelegate.getApplicationLocales().toLanguageTags()
    }
}