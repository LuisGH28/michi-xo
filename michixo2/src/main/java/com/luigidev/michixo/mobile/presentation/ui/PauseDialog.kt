package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.CatOpponent
import com.luigidev.michixo.mobile.presentation.theme.MichiGameTheme
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.ThemeManager
import com.luigidev.michixo.mobile.presentation.theme.ThemeType

@Composable
fun PauseDialog(
    isVolumeEnabled: Boolean,
    gameTheme: MichiGameTheme? = null,
    opponent: CatOpponent? = null,
    onResume: () -> Unit,
    onExitHome: () -> Unit,
    onOpenSettings: () -> Unit,
    onToggleVolume: () -> Unit,
    onOpenInfo: () -> Unit
) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    val characterName = opponent?.displayName() ?: stringResource(R.string.cat_luz_short)
    val themedMode = activeTheme.themeType != ThemeType.Luz
    val nightMode = activeTheme.themeType == ThemeType.Salem
    val dialogBackground = if (themedMode) activeTheme.pauseCardColor else MichiSoftPink
    val cardColor = if (themedMode) activeTheme.boardColor else MichiWhite
    val primaryText = if (themedMode) activeTheme.pauseTextColor else MichiSoftBrown
    val secondaryText = if (themedMode) activeTheme.pauseAccentColor else MichiButton
    val mutedText = if (themedMode) activeTheme.pauseTextColor.copy(alpha = 0.78f) else MichiTextPrimary
    val mainButtonColor = if (themedMode) activeTheme.pausePrimaryButtonColor else MichiButton
    val mainButtonContent = if (nightMode) activeTheme.backgroundColor else MichiWhite
    val secondaryButtonColor = if (themedMode) activeTheme.pauseSecondaryButtonColor else MichiDeepPink
    val secondaryButtonContent = if (themedMode) activeTheme.pauseTextColor else MichiButton

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(0.86f)
            .widthIn(max = 340.dp),
        onDismissRequest = onResume,
        confirmButton = {},
        dismissButton = {},
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (themedMode) {
                    ThemeBackgroundLayer(
                        theme = activeTheme,
                        modifier = Modifier.matchParentSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Surface(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 44.dp, height = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (themedMode) activeTheme.pauseAccentColor.copy(alpha = 0.62f) else MichiPink
                ) {}

                Spacer(modifier = Modifier.height(12.dp))

                Box {
                    val avatarSize = 96.dp
                    if (opponent == null) {
                        Image(
                            painter = painterResource(id = R.drawable.luz_bored),
                            contentDescription = stringResource(R.string.cd_luz_bored),
                            modifier = Modifier
                                .size(avatarSize)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        CatAvatar(
                            name = characterName,
                            opponent = opponent,
                            modifier = Modifier.size(avatarSize)
                        )
                    }

                    Surface(
                        modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp),
                        shape = CircleShape,
                        color = if (themedMode) cardColor else MichiButton
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Pets,
                                contentDescription = stringResource(R.string.cd_pets),
                                tint = if (themedMode) activeTheme.pauseAccentColor else MichiWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = characterName,
                    fontFamily = MichiFont,
                    fontSize = 22.sp,
                    color = primaryText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.pause_subtitle_character, characterName),
                    color = secondaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onResume,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainButtonColor,
                        contentColor = mainButtonContent
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.PauseCircle,
                            contentDescription = stringResource(R.string.resume),
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = stringResource(R.string.resume),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onExitHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = secondaryButtonColor,
                        contentColor = secondaryButtonContent
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = stringResource(R.string.exit_home),
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = stringResource(R.string.exit_home),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PauseActionIcon(
                        icon = Icons.Filled.Settings,
                        label = stringResource(R.string.settings_title),
                        gameTheme = activeTheme,
                        onClick = onOpenSettings
                    )

                    PauseActionIcon(
                        icon = if (isVolumeEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                        label = if (isVolumeEnabled) stringResource(R.string.sound_on) else stringResource(R.string.muted),
                        gameTheme = activeTheme,
                        onClick = onToggleVolume
                    )

                    PauseActionIcon(
                        icon = Icons.Filled.Info,
                        label = stringResource(R.string.info),
                        gameTheme = activeTheme,
                        onClick = onOpenInfo
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.paused),
                        color = mutedText,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp
                    )
                }
            }
        },
        containerColor = dialogBackground,
        shape = RoundedCornerShape(26.dp)
    )
}

@Composable
fun PauseActionIcon(
    icon: ImageVector,
    label: String,
    gameTheme: MichiGameTheme? = null,
    onClick: () -> Unit
) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    val themedMode = activeTheme.themeType != ThemeType.Luz
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = if (themedMode) activeTheme.boardColor else MichiWhite,
            shadowElevation = 3.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (themedMode) activeTheme.pauseAccentColor else MichiSoftBrown,
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            color = if (themedMode) activeTheme.pauseTextColor.copy(alpha = 0.78f) else MichiTextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PauseSettingsDialog(
    musicEnabled: Boolean,
    vibrationEnabled: Boolean,
    gameTheme: MichiGameTheme? = null,
    onDismiss: () -> Unit,
    onMusicToggle: () -> Unit,
    onVibrationToggle: () -> Unit
) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    val themedMode = activeTheme.themeType != ThemeType.Luz
    val nightMode = activeTheme.themeType == ThemeType.Salem
    val dialogBackground = if (themedMode) activeTheme.pauseCardColor else MichiSoftPink
    val titleColor = if (themedMode) activeTheme.pauseTextColor else MichiSoftBrown
    val textColor = if (themedMode) activeTheme.pauseTextColor.copy(alpha = 0.82f) else MichiTextPrimary
    val buttonColor = if (themedMode) activeTheme.pausePrimaryButtonColor else MichiButton
    val buttonContent = if (nightMode) activeTheme.backgroundColor else MichiWhite

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = buttonContent
                )
            ) {
                Text(stringResource(R.string.done))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.quick_settings),
                color = titleColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.music), fontSize = 16.sp, color = textColor)

                    Switch(
                        checked = musicEnabled,
                        onCheckedChange = { onMusicToggle() }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.vibration), fontSize = 16.sp, color = textColor)

                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = { onVibrationToggle() }
                    )
                }
            }
        },
        containerColor = dialogBackground
    )
}

@Composable
fun PauseInfoDialog(
    gameTheme: MichiGameTheme? = null,
    opponent: CatOpponent? = null,
    onDismiss: () -> Unit
) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    val themedMode = activeTheme.themeType != ThemeType.Luz
    val nightMode = activeTheme.themeType == ThemeType.Salem
    val rivalName = opponent?.displayName() ?: stringResource(R.string.cat_luz_short)
    val dialogBackground = if (themedMode) activeTheme.pauseCardColor else MichiSoftPink
    val titleColor = if (themedMode) activeTheme.pauseTextColor else MichiSoftBrown
    val textColor = if (themedMode) activeTheme.pauseTextColor.copy(alpha = 0.82f) else MichiTextPrimary
    val buttonColor = if (themedMode) activeTheme.pausePrimaryButtonColor else MichiButton
    val buttonContent = if (nightMode) activeTheme.backgroundColor else MichiWhite

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = buttonContent
                )
            ) {
                Text(stringResource(R.string.close))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.about_michi),
                color = titleColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerLegendItem(
                        label = stringResource(R.string.player_yarn),
                        isPlayer = true,
                        gameTheme = activeTheme
                    )

                    PlayerLegendItem(
                        label = rivalName,
                        isPlayer = false,
                        gameTheme = activeTheme
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.info_you_are_x),
                    fontSize = 15.sp,
                    color = textColor
                )

                Text(
                    text = stringResource(R.string.info_rival_plays_paw, rivalName),
                    fontSize = 15.sp,
                    color = textColor
                )

                Text(
                    text = stringResource(R.string.info_make_three),
                    fontSize = 15.sp,
                    color = textColor
                )

                Text(
                    text = stringResource(R.string.info_tap_tile),
                    fontSize = 15.sp,
                    color = textColor
                )

                Text(
                    text = stringResource(R.string.info_version),
                    fontSize = 15.sp,
                    color = textColor
                )

                Text(
                    text = stringResource(R.string.info_author),
                    fontSize = 15.sp,
                    color = textColor
                )
            }
        },
        containerColor = dialogBackground
    )
}

@Composable
fun PlayerLegendItem(
    label: String,
    isPlayer: Boolean,
    gameTheme: MichiGameTheme? = null
) {
    val activeTheme = gameTheme ?: ThemeManager.themeFor(ThemeType.Luz)
    val themedMode = activeTheme.themeType != ThemeType.Luz
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(54.dp),
            shape = CircleShape,
            color = if (themedMode) activeTheme.boardColor else MichiWhite,
            shadowElevation = 3.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isPlayer) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_yarn),
                        contentDescription = label,
                        modifier = Modifier.size(34.dp),
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Pets,
                        contentDescription = label,
                        modifier = Modifier.size(32.dp),
                        tint = if (themedMode) activeTheme.pauseAccentColor else MichiButton
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 13.sp,
            color = if (themedMode) activeTheme.pauseTextColor else MichiSoftBrown,
            fontWeight = FontWeight.Bold
        )
    }
}
