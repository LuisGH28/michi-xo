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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.GameViewModel
import com.luigidev.michixo.mobile.presentation.theme.MichiAccent
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiTextPrimary
import com.luigidev.michixo.mobile.presentation.theme.MichiXOTheme

@Composable
fun HomeScreen(vm: GameViewModel) {
    HomeScreenContent(
        onPlayClick = { vm.startGame() },
        onSettingsClick = { vm.goToSettings() }
    )
}

@Composable
fun HomeScreenContent(
    onPlayClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MichiSoftPink)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(26.dp),
                        shape = CircleShape,
                        color = MichiPink
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Pets,
                                contentDescription = "Michi icon",
                                modifier = Modifier.size(14.dp),
                                tint = MichiSoftBrown
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "MichiXO",
                        fontFamily = MichiFont,
                        fontSize = 16.sp,
                        color = MichiSoftBrown
                    )
                }

                Surface(
                    modifier = Modifier.size(26.dp),
                    shape = CircleShape,
                    color = MichiDeepPink
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = stringResource(R.string.notifications),
                            modifier = Modifier.size(14.dp),
                            tint = MichiSoftBrown
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Surface(
                modifier = Modifier.size(width = 210.dp, height = 250.dp),
                shape = RoundedCornerShape(25.dp),
                color = MichiDeepPink
            ) {
                Box(
                    modifier = Modifier.padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.luz_hs),
                        contentDescription = "Luz",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(22.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "MichiXO",
                fontFamily = MichiFont,
                fontSize = 40.sp,
                color = MichiSoftBrown
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.lest_play),
                fontSize = 13.sp,
                color = MichiTextPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onPlayClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MichiButton,
                    contentColor = MichiTextPrimary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.play),
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.play),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.settings_title),
                    icon = Icons.Filled.Settings,
                    onClick = onSettingsClick
                )
            }

            Spacer(modifier = Modifier.weight(1f))


        }
    }
}

@Composable
private fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MichiPink,
            contentColor = MichiSoftBrown
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    selected: Boolean
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(22.dp),
        tint = if (selected) MichiAccent else MichiTextPrimary
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MichiXOTheme {
        HomeScreenContent(
            onPlayClick = {}
        )
    }
}