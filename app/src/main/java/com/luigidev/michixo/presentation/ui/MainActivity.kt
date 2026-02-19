package com.luigidev.michixo.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luigidev.michixo.presentation.GameViewModel
import com.luigidev.michixo.presentation.theme.MichiXOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MichiXOTheme {
                val vm: GameViewModel = viewModel()
                TicTacToeScreen(vm)
            }
        }
    }
}
