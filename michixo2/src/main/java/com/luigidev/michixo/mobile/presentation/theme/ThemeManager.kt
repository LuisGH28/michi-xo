package com.luigidev.michixo.mobile.presentation.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.luigidev.michixo.mobile.R

object ThemeManager {
    private const val PREFS_NAME = "michixo_theme"
    private const val KEY_THEME_TYPE = "selected_theme_type"

    fun restoreThemeType(context: Context): ThemeType {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return ThemeType.fromId(prefs.getString(KEY_THEME_TYPE, ThemeType.Luz.id))
    }

    fun persistThemeType(context: Context, themeType: ThemeType) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME_TYPE, themeType.id)
            .apply()
    }

    @Composable
    fun themeFor(themeType: ThemeType): MichiGameTheme {
        return when (themeType) {
            ThemeType.Luz -> MichiGameTheme(
                themeType = ThemeType.Luz,
                themeName = "Luz",
                backgroundColor = colorResource(R.color.michi_soft_pink),
                boardColor = colorResource(R.color.michi_board_pink),
                primaryColor = colorResource(R.color.michi_button_pink),
                secondaryColor = colorResource(R.color.michi_pink),
                xIcon = R.drawable.ic_yarn,
                oIcon = R.drawable.ic_yarn,
                xColor = Color.Unspecified,
                oColor = colorResource(R.color.michi_o_pink),
                victoryColor = colorResource(R.color.michi_button_pink),
                pauseCardColor = colorResource(R.color.michi_soft_pink),
                pauseTextColor = colorResource(R.color.michi_soft_brown),
                pausePrimaryButtonColor = colorResource(R.color.michi_button_pink),
                pauseSecondaryButtonColor = colorResource(R.color.michi_deep_pink),
                pauseAccentColor = colorResource(R.color.michi_pink),
                useLibraryPawForO = true
            )

            ThemeType.Lily -> MichiGameTheme(
                themeType = ThemeType.Lily,
                themeName = "Lily",
                backgroundColor = colorResource(R.color.lily_garden_background),
                boardColor = colorResource(R.color.lily_garden_card),
                primaryColor = colorResource(R.color.michi_lily_accent),
                secondaryColor = colorResource(R.color.michi_soft_pink),
                xIcon = R.drawable.ic_yarn,
                oIcon = R.drawable.ic_michi_flower,
                xColor = Color.Unspecified,
                oColor = colorResource(R.color.lily_flower_accent),
                victoryColor = colorResource(R.color.michi_lily_accent),
                pauseCardColor = colorResource(R.color.lily_garden_card),
                pauseTextColor = colorResource(R.color.michi_soft_brown),
                pausePrimaryButtonColor = colorResource(R.color.lily_flower_accent),
                pauseSecondaryButtonColor = colorResource(R.color.michi_soft_pink),
                pauseAccentColor = colorResource(R.color.michi_lily_accent)
            )

            ThemeType.Coco -> MichiGameTheme(
                themeType = ThemeType.Coco,
                themeName = "Coco",
                backgroundColor = colorResource(R.color.coco_sky_background),
                boardColor = colorResource(R.color.michi_coco_board),
                primaryColor = colorResource(R.color.teal_700),
                secondaryColor = colorResource(R.color.coco_cloud),
                xIcon = R.drawable.ic_yarn,
                oIcon = R.drawable.ic_michi_sun,
                xColor = Color.Unspecified,
                oColor = colorResource(R.color.teal_700),
                victoryColor = colorResource(R.color.teal_700),
                pauseCardColor = colorResource(R.color.coco_cloud),
                pauseTextColor = colorResource(R.color.michi_soft_brown),
                pausePrimaryButtonColor = colorResource(R.color.teal_700),
                pauseSecondaryButtonColor = colorResource(R.color.coco_sun_soft),
                pauseAccentColor = colorResource(R.color.teal_700)
            )

            ThemeType.Salem -> MichiGameTheme(
                themeType = ThemeType.Salem,
                themeName = "Salem",
                backgroundColor = colorResource(R.color.salem_galaxy_background),
                boardColor = colorResource(R.color.salem_galaxy_card),
                primaryColor = colorResource(R.color.salem_moonlight),
                secondaryColor = colorResource(R.color.michi_salem_cell),
                xIcon = R.drawable.ic_yarn,
                oIcon = R.drawable.ic_michi_moon,
                xColor = Color.Unspecified,
                oColor = colorResource(R.color.salem_moonlight),
                victoryColor = colorResource(R.color.salem_moonlight),
                pauseCardColor = colorResource(R.color.salem_galaxy_card),
                pauseTextColor = colorResource(R.color.salem_star),
                pausePrimaryButtonColor = colorResource(R.color.salem_moonlight),
                pauseSecondaryButtonColor = colorResource(R.color.michi_salem_cell),
                pauseAccentColor = colorResource(R.color.salem_star)
            )
        }
    }
}
