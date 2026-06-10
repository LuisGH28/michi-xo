package com.luigidev.michixo.mobile.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luigidev.michixo.mobile.audio.MusicManager
import com.luigidev.michixo.mobile.data.NotificationPreferences
import com.luigidev.michixo.mobile.data.UpdatePreferences
import com.luigidev.michixo.mobile.data.UserBehaviorStore
import com.luigidev.michixo.mobile.notifications.NotificationHelper
import com.luigidev.michixo.mobile.notifications.NotificationScheduler
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.Screen
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme
import com.luigidev.michixo.mobile.presentation.theme.ThemeManager

class MainActivity : AppCompatActivity() {
    private companion object {
        const val KEY_HAS_SEEN_SUPER_MICHI_INTRO = "has_seen_super_michi_intro"
        const val UPDATE_PLAY_STORE_URL =
            "https://play.google.com/store/apps/details?id=com.luigidev.michixo.mobile&hl=es_MX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        setContent {
            MichiXOTheme {
                val vm: GameViewModel = viewModel()
                val context = LocalContext.current
                val uiState = vm.uiState.collectAsStateWithLifecycle().value
                var themeRestored by remember { mutableStateOf(false) }

                val behaviorStore = remember {
                    UserBehaviorStore(context)
                }

                val notificationPreferences = remember {
                    NotificationPreferences(context)
                }
                val updatePreferences = remember {
                    UpdatePreferences(context)
                }
                val introPreferences = remember {
                    context.getSharedPreferences("michixo_intro", 0)
                }
                var showUpdateDialog by remember { mutableStateOf(false) }

                vm.onGameFinished = { result ->
                    behaviorStore.saveGamePlayed(result)
                }

                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted ->
                    notificationPreferences.setPermissionAsked()

                    if (granted) {
                        notificationPreferences.setNotificationsEnabled(true)
                        vm.setNotificationsEnabled(true)
                        NotificationScheduler.scheduleDailyReminder(context)
                    } else {
                        notificationPreferences.setNotificationsEnabled(false)
                        vm.setNotificationsEnabled(false)
                        NotificationScheduler.cancelDailyReminder(context)
                    }
                }

                LaunchedEffect(Unit) {
                    vm.setThemeType(ThemeManager.restoreThemeType(context))
                    vm.setHasSeenSuperMichiIntro(
                        introPreferences.getBoolean(KEY_HAS_SEEN_SUPER_MICHI_INTRO, false)
                    )
                    themeRestored = true

                    if (!introPreferences.getBoolean("home_family_greeting_seen", false)) {
                        introPreferences.edit()
                            .putBoolean("home_family_greeting_seen", true)
                            .apply()
                        vm.showHomeFamilyGreeting()
                    }

                    val savedNotificationsEnabled =
                        notificationPreferences.areNotificationsEnabled()

                    vm.setNotificationsEnabled(savedNotificationsEnabled)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionGranted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                        when {
                            permissionGranted && savedNotificationsEnabled -> {
                                NotificationScheduler.scheduleDailyReminder(context)
                            }

                            !permissionGranted && !notificationPreferences.wasPermissionAsked() -> {
                                notificationPermissionLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            }

                            !permissionGranted -> {
                                notificationPreferences.setNotificationsEnabled(false)
                                vm.setNotificationsEnabled(false)
                                NotificationScheduler.cancelDailyReminder(context)
                            }
                        }
                    } else {
                        if (savedNotificationsEnabled) {
                            NotificationScheduler.scheduleDailyReminder(context)
                        }
                    }

                    showUpdateDialog = updatePreferences.shouldShowUpdateDialog()
                }

                LaunchedEffect(themeRestored, uiState.selectedThemeType) {
                    if (themeRestored) {
                        ThemeManager.persistThemeType(context, uiState.selectedThemeType)
                    }
                }

                LaunchedEffect(uiState.musicEnabled, uiState.screen) {
                    if (uiState.musicEnabled && uiState.screen == Screen.GAME) {
                        MusicManager.start(context)
                    } else {
                        MusicManager.pause()
                    }
                }

                LaunchedEffect(uiState.screen) {
                    if (uiState.screen == Screen.SUPER_INTRO &&
                        !introPreferences.getBoolean("super_family_greeting_seen", false)
                    ) {
                        introPreferences.edit()
                            .putBoolean("super_family_greeting_seen", true)
                            .apply()
                        vm.showSuperFamilyGreeting()
                    }
                }

                TicTacToeScreen(
                    vm = vm,
                    onExitApp = { finish() },
                    onSuperMichiIntroDismissed = {
                        introPreferences.edit()
                            .putBoolean(KEY_HAS_SEEN_SUPER_MICHI_INTRO, true)
                            .apply()
                        vm.setHasSeenSuperMichiIntro(true)
                    },
                    onNotificationsToggle = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val permissionGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED

                                if (permissionGranted) {
                                    notificationPreferences.setNotificationsEnabled(true)
                                    vm.setNotificationsEnabled(true)
                                    NotificationScheduler.scheduleDailyReminder(context)
                                } else {
                                    notificationPermissionLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }
                            } else {
                                notificationPreferences.setNotificationsEnabled(true)
                                vm.setNotificationsEnabled(true)
                                NotificationScheduler.scheduleDailyReminder(context)
                            }
                        } else {
                            notificationPreferences.setNotificationsEnabled(false)
                            vm.setNotificationsEnabled(false)
                            NotificationScheduler.cancelDailyReminder(context)
                        }
                    }
                )

                if (showUpdateDialog) {
                    UpdateAvailableDialog(
                        onUpdateNow = {
                            showUpdateDialog = false
                            openPlayStoreUpdate(context)
                        },
                        onDismiss = {
                            updatePreferences.saveDismissalTimestamp()
                            showUpdateDialog = false
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stop()
    }

    private fun openPlayStoreUpdate(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UPDATE_PLAY_STORE_URL))
        context.startActivity(intent)
    }
}
