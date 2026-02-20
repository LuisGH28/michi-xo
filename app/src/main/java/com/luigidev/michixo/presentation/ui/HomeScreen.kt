package com.luigidev.michixo.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.luigidev.michixo.R
import com.luigidev.michixo.presentation.GameViewModel
import com.luigidev.michixo.presentation.theme.MichiPink

@Composable
fun HomeScreen(vm: GameViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colors.background,
                        MaterialTheme.colors.surface
                    )
                )
            )
    ) {

        Image(
            painter = painterResource(id = R.drawable.luz_home),
            contentDescription = "Luz",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-22).dp)
                .size(150.dp)
                .alpha(0.98f)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-6).dp)
                .padding(start = 66.dp, end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Michi XO",
                color = MaterialTheme.colors.onPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tic Tac Toe\ncontra IA",
                color = MaterialTheme.colors.onSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { vm.startGame() },
                modifier = Modifier
                    .height(42.dp)
                    .fillMaxWidth(0.65f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color(0xFF4A4A4A)
                )
            ) {
                Text(text = "Jugar")
            }
        }
    }
}
