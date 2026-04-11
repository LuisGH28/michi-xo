package com.luigidev.michixo.mobile.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luigidev.michixo.mobile.data.UserBehaviorStore
import com.luigidev.michixo.mobile.notifications.NotificationHelper
import com.luigidev.michixo.mobile.notifications.NotificationScheduler
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        setContent {
            MichiXOTheme {
                val vm: GameViewModel = viewModel()
                val context = LocalContext.current
                val behaviorStore = UserBehaviorStore(context)

                vm.onGameFinished = { result ->
                    behaviorStore.saveGamePlayed(result)
                }

                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (granted) {
                        vm.setNotificationsEnabled(true)
                        NotificationScheduler.scheduleDailyReminder(context)
                    } else {
                        vm.setNotificationsEnabled(false)
                    }
                }

                TicTacToeScreen(
                    vm = vm,
                    onExitApp = { finish() },
                    onNotificationsToggle = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val alreadyGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED

                                if (alreadyGranted) {
                                    vm.setNotificationsEnabled(true)
                                    NotificationScheduler.scheduleDailyReminder(context)
                                } else {
                                    notificationPermissionLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }
                            } else {
                                vm.setNotificationsEnabled(true)
                                NotificationScheduler.scheduleDailyReminder(context)
                            }
                        } else {
                            vm.setNotificationsEnabled(false)
                            NotificationScheduler.cancelDailyReminder(context)
                        }
                    }
                )
            }
        }
    }
}