package com.luigidev.michixo.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.luigidev.michixo.presentation.GameViewModel

@Composable
fun HomeScreen(vm: GameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Text(
            "Michi XO",
            color = Color(0xFFB76E79)

        )
        Spacer(Modifier.height(6.dp))

        Text(
            "Tic Tac Toe contra IA",
            color = Color(0XFF8D6E63)
            )
        Spacer(Modifier.height(12.dp))

            Button(
                onClick = { vm.startGame() },
                modifier = Modifier.height(40.dp),
                colors = androidx.wear.compose.material.ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFFC1D6),
                    contentColor = Color(0xFF4A4A4A)
                )
            ) {
                Text("Jugar")
            }

        }
    }
}
