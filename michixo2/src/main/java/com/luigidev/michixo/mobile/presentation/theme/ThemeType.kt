package com.luigidev.michixo.mobile.presentation.theme

enum class ThemeType(val id: String) {
    Luz("luz"),
    Lily("lily"),
    Coco("coco"),
    Salem("salem");

    companion object {
        fun fromId(id: String?): ThemeType {
            return entries.firstOrNull { it.id == id } ?: Luz
        }
    }
}
