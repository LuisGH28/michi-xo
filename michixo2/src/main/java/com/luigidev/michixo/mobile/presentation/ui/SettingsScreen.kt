package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.luigidev.michixo.mobile.presentation.theme.MichiSwitchOff
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme
import com.luigidev.michixo.mobile.presentation.util.LanguageManager

@Composable
fun SettingsScreen(
    musicEnabled: Boolean,
    vibrationEnabled: Boolean,
    notificationsEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MichiSoftBrown
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.settings_title),
                    fontFamily = MichiFont,
                    fontSize = 28.sp,
                    color = MichiSoftBrown,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = MichiDeepPink
            ) {
                Image(
                    painter = painterResource(id = R.drawable.luz_settings),
                    contentDescription = stringResource(R.string.cd_luz_settings),
                    modifier = Modifier.clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.settings_subtitle),
                fontSize = 16.sp,
                color = MichiTextPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(28.dp))

            SettingsOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.MusicNote,
                        contentDescription = stringResource(R.string.music),
                        tint = MichiButton
                    )
                },
                title = stringResource(R.string.music),
                subtitle = stringResource(R.string.music_subtitle),
                checked = musicEnabled,
                onCheckedChange = onMusicToggle
            )

            Spacer(modifier = Modifier.height(14.dp))

            SettingsOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Vibration,
                        contentDescription = stringResource(R.string.vibration),
                        tint = MichiButton
                    )
                },
                title = stringResource(R.string.vibration),
                subtitle = stringResource(R.string.vibration_subtitle),
                checked = vibrationEnabled,
                onCheckedChange = onVibrationToggle
            )

            Spacer(modifier = Modifier.height(14.dp))

            SettingsOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = stringResource(R.string.notifications),
                        tint = MichiButton
                    )
                },
                title = stringResource(R.string.notifications),
                subtitle = stringResource(R.string.notifications_subtitle),
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsToggle
            )

            Spacer(modifier = Modifier.height(14.dp))

            LanguageCard()

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MichiButton,
                    contentColor = MichiWhite
                )
            ) {
                Text(
                    text = stringResource(R.string.done),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun SettingsOptionCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MichiDeepPink,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = MichiPink
            ) {
                Box(contentAlignment = Alignment.Center) {
                    icon()
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = MichiSoftBrown,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MichiTextPrimary
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MichiWhite,
                    checkedTrackColor = MichiButton,
                    uncheckedThumbColor = MichiWhite,
                    uncheckedTrackColor = MichiSwitchOff
                )
            )
        }
    }
}

@Composable
fun LanguageCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MichiDeepPink,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.language),
                fontSize = 18.sp,
                color = MichiSoftBrown,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.language_subtitle),
                fontSize = 13.sp,
                color = MichiTextPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { LanguageManager.setLanguage("en") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.english))
                }

                OutlinedButton(
                    onClick = { LanguageManager.setLanguage("es") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.spanish))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    var musicEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    MichiXOTheme {
        SettingsScreen(
            musicEnabled = musicEnabled,
            vibrationEnabled = vibrationEnabled,
            notificationsEnabled = notificationsEnabled,
            onMusicToggle = { musicEnabled = it },
            onVibrationToggle = { vibrationEnabled = it },
            onNotificationsToggle = { notificationsEnabled = it },
            onBackClick = {}
        )
    }
}