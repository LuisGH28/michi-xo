package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo_core.model.Difficulty

@Composable
fun DifficultyDialog(
    selectedDifficulty: Difficulty,
    onDismiss: () -> Unit,
    onConfirm: (Difficulty) -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedDifficulty) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MichiSoftPink,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = stringResource(R.string.difficulty_dialog_title),
                fontFamily = MichiFont,
                fontSize = 22.sp,
                color = MichiSoftBrown
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.difficulty_dialog_subtitle),
                    fontSize = 13.sp,
                    color = MichiTextPrimary
                )

                DifficultyOptionCard(
                    title = stringResource(R.string.difficulty_easy),
                    description = stringResource(R.string.difficulty_easy_desc),
                    icon = Icons.Filled.Casino,
                    selected = currentSelection == Difficulty.EASY,
                    onClick = { currentSelection = Difficulty.EASY }
                )

                DifficultyOptionCard(
                    title = stringResource(R.string.difficulty_medium),
                    description = stringResource(R.string.difficulty_medium_desc),
                    icon = Icons.Filled.Psychology,
                    selected = currentSelection == Difficulty.MEDIUM,
                    onClick = { currentSelection = Difficulty.MEDIUM }
                )

                DifficultyOptionCard(
                    title = stringResource(R.string.difficulty_hard),
                    description = stringResource(R.string.difficulty_hard_desc),
                    icon = Icons.Filled.Bolt,
                    selected = currentSelection == Difficulty.HARD,
                    onClick = { currentSelection = Difficulty.HARD }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(currentSelection) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MichiButton,
                    contentColor = MichiTextPrimary
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.start_game),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MichiSoftBrown
                )
            }
        }
    )
}

@Composable
private fun DifficultyOptionCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) MichiPink else MichiSoftPink
    val borderColor = if (selected) MichiDeepPink else MichiPink

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier.size(38.dp),
            shape = CircleShape,
            color = MichiDeepPink
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MichiSoftBrown,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MichiSoftBrown
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MichiTextPrimary
            )
        }
    }
}