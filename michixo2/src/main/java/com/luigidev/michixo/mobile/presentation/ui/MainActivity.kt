package com.luigidev.michixo.mobile.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luigidev.michixo.mobile.audio.MusicManager
import com.luigidev.michixo.mobile.data.NotificationPreferences
import com.luigidev.michixo.mobile.data.UserBehaviorStore
import com.luigidev.michixo.mobile.notifications.NotificationHelper
import com.luigidev.michixo.mobile.notifications.NotificationScheduler
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.Screen
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        setContent {
            MichiXOTheme {
                val vm: GameViewModel = viewModel()
                val context = LocalContext.current
                val uiState = vm.uiState.collectAsStateWithLifecycle().value

                val behaviorStore = remember {
                    UserBehaviorStore(context)
                }

                val notificationPreferences = remember {
                    NotificationPreferences(context)
                }
                val introPreferences = remember {
                    context.getSharedPreferences("michixo_intro", 0)
                }

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
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stop()
    }
}
