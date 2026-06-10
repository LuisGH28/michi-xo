package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun UpdateAvailableDialog(
    onUpdateNow: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .widthIn(max = 360.dp),
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UpdateLuzHeader()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.update_title),
                    fontFamily = MichiFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MichiSoftBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.update_message),
                    fontSize = 15.sp,
                    color = MichiTextPrimary,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onUpdateNow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MichiButton,
                        contentColor = MichiWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.SystemUpdate,
                        contentDescription = stringResource(R.string.update_now),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = stringResource(R.string.update_now),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.update_later),
                        color = MichiSoftBrown,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        containerColor = MichiSoftPink,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
private fun UpdateLuzHeader() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .padding(top = 34.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = MichiWhite,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 94.dp, top = 14.dp, end = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = stringResource(R.string.update_title),
                    tint = MichiButton,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = stringResource(R.string.update_luz_greeting),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MichiSoftBrown
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.luz_hs),
            contentDescription = stringResource(R.string.luz_image),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 14.dp)
                .size(92.dp)
                .clip(CircleShape)
                .background(MichiDeepPink, CircleShape)
                .border(4.dp, MichiPink, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
