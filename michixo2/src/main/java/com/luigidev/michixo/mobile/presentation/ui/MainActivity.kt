package com.luigidev.michixo.mobile.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MichiXOTheme {
                val vm: GameViewModel = viewModel()
                TicTacToeScreen(
                    vm = vm,
                    onExitApp = { finish() }
                )
            }
        }
    }
}