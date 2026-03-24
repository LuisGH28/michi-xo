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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import androidx.compose.ui.res.stringResource

@Composable
fun PauseDialog(
    isVolumeEnabled: Boolean,
    onResume: () -> Unit,
    onExitHome: () -> Unit,
    onOpenSettings: () -> Unit,
    onToggleVolume: () -> Unit,
    onOpenInfo: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        confirmButton = {},
        dismissButton = {},
        text = {
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
                    color = MichiPink
                ) {}

                Spacer(modifier = Modifier.height(18.dp))

                Box {
                    Image(
                        painter = painterResource(id = R.drawable.luz_bored),
                        contentDescription = stringResource(R.string.cd_luz_bored),
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp),
                        shape = CircleShape,
                        color = MichiButton
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Pets,
                                contentDescription = stringResource(R.string.cd_pets),
                                tint = MichiWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.pause_title),
                    fontFamily = MichiFont,
                    fontSize = 24.sp,
                    color = MichiSoftBrown,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.pause_subtitle),
                    color = MichiButton,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onResume,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MichiButton,
                        contentColor = MichiWhite
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PauseCircle,
                            contentDescription = stringResource(R.string.resume),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.resume),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onExitHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MichiDeepPink,
                        contentColor = MichiButton
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = stringResource(R.string.exit_home),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.exit_home),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PauseActionIcon(
                        icon = Icons.Filled.Settings,
                        label = stringResource(R.string.settings_title),
                        onClick = onOpenSettings
                    )

                    PauseActionIcon(
                        icon = if (isVolumeEnabled) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                        label = if (isVolumeEnabled) stringResource(R.string.sound_on) else stringResource(R.string.muted),
                        onClick = onToggleVolume
                    )

                    PauseActionIcon(
                        icon = Icons.Filled.Info,
                        label = stringResource(R.string.info),
                        onClick = onOpenInfo
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.paused),
                    color = MichiTextPrimary,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp
                )
            }
        },
        containerColor = MichiSoftPink,
        shape = RoundedCornerShape(32.dp)
    )
}

@Composable
fun PauseActionIcon(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.size(46.dp),
            shape = CircleShape,
            color = MichiWhite,
            shadowElevation = 3.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MichiSoftBrown,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = MichiTextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PauseSettingsDialog(
    musicEnabled: Boolean,
    vibrationEnabled: Boolean,
    onDismiss: () -> Unit,
    onMusicToggle: () -> Unit,
    onVibrationToggle: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MichiButton,
                    contentColor = MichiWhite
                )
            ) {
                Text(stringResource(R.string.done))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.quick_settings),
                color = MichiSoftBrown
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
                    Text("Music", fontSize = 16.sp, color = MichiTextPrimary)
                    Switch(
                        checked = musicEnabled,
                        onCheckedChange = { onMusicToggle() }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Vibration", fontSize = 16.sp, color = MichiTextPrimary)
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = { onVibrationToggle() }
                    )
                }
            }
        },
        containerColor = MichiSoftPink
    )
}

@Composable
fun PauseInfoDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MichiButton,
                    contentColor = MichiWhite
                )
            ) {
                Text(stringResource(R.string.close))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.about_michi),
                color = MichiSoftBrown
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(R.string.info_you_are_x), fontSize = 15.sp, color = MichiTextPrimary)
                Text(stringResource(R.string.info_luz_rival), fontSize = 15.sp, color = MichiTextPrimary)
                Text(stringResource(R.string.info_make_three), fontSize = 15.sp, color = MichiTextPrimary)
                Text(stringResource(R.string.info_tap_tile), fontSize = 15.sp, color = MichiTextPrimary)
                Text(stringResource(R.string.info_version), fontSize = 15.sp, color = MichiTextPrimary)
                Text(stringResource(R.string.info_author), fontSize = 15.sp, color = MichiTextPrimary)
            }
        },
        containerColor = MichiSoftPink
    )
}