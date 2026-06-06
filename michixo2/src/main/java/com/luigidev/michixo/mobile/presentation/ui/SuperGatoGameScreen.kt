package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.CatOpponent
import com.luigidev.michixo.mobile.presentation.GameUiState
import com.luigidev.michixo.mobile.presentation.theme.MichiBoard
import com.luigidev.michixo.mobile.presentation.theme.MichiButton
import com.luigidev.michixo.mobile.presentation.theme.MichiDeepPink
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiGameTheme
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftPink
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite
import com.luigidev.michixo.mobile.presentation.theme.ThemeType
import com.luigidev.michixo.model.Player

@Composable
fun SuperGatoGameContent(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    isLandscape: Boolean,
    onCellTap: (Int, Int) -> Unit,
    onGreetingDismiss: () -> Unit,
    onPauseClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp
        val boardModifier = when {
            isLandscape -> Modifier
                .fillMaxHeight(if (isTablet) 0.94f else 0.98f)
                .aspectRatio(1f)

            isTablet -> Modifier
                .fillMaxWidth(0.74f)
                .widthIn(max = 620.dp)

            else -> Modifier.fillMaxWidth()
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(if (isTablet) 26.dp else 16.dp)
                ) {
                    SuperSidePanel(
                        uiState = uiState,
                        gameTheme = gameTheme,
                        onPauseClick = onPauseClick,
                        onSettingsClick = onSettingsClick,
                        compact = !isTablet,
                        modifier = Modifier.weight(if (isTablet) 0.72f else 0.86f)
                    )
                    Box(
                        modifier = Modifier.weight(if (isTablet) 1.28f else 1.14f),
                        contentAlignment = Alignment.Center
                    ) {
                        SuperGatoBoard(
                            uiState = uiState,
                            gameTheme = gameTheme,
                            onCellTap = onCellTap,
                            modifier = boardModifier
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameHeader(gameTheme)
                    Spacer(modifier = Modifier.height(if (isTablet) 18.dp else 12.dp))
                    SuperMatchHeader(uiState = uiState, gameTheme = gameTheme, compact = !isTablet)
                    Spacer(modifier = Modifier.height(if (isTablet) 18.dp else 12.dp))
                    SuperGatoBoard(
                        uiState = uiState,
                        gameTheme = gameTheme,
                        onCellTap = onCellTap,
                        modifier = boardModifier
                    )
                    Spacer(modifier = Modifier.height(if (isTablet) 18.dp else 12.dp))
                    SuperStatus(uiState = uiState, gameTheme = gameTheme)
                    Spacer(modifier = Modifier.height(if (isTablet) 18.dp else 14.dp))
                    GameActions(
                        gameTheme = gameTheme,
                        onPauseClick = onPauseClick,
                        onSettingsClick = onSettingsClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uiState.showSuperGreeting) {
                SuperGreetingOverlay(
                    opponent = uiState.opponent,
                    gameTheme = gameTheme,
                    isLandscape = isLandscape,
                    isTablet = isTablet,
                    onDismiss = onGreetingDismiss,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun SuperGreetingOverlay(
    opponent: CatOpponent,
    gameTheme: MichiGameTheme,
    isLandscape: Boolean,
    isTablet: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nightMode = gameTheme.themeName == "Salem"
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.18f))
            .clickable { onDismiss() }
            .padding(if (isTablet) 26.dp else 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(if (isTablet) 0.82f else 1f)
                .widthIn(max = 860.dp)
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(26.dp),
            color = if (nightMode) gameTheme.boardColor else MichiSoftPink,
            shadowElevation = 8.dp
        ) {
            val imageSize = when {
                isTablet -> 220.dp
                isLandscape -> 160.dp
                else -> 132.dp
            }

            Row(
                modifier = Modifier.padding(if (isTablet) 22.dp else 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(if (isTablet) 22.dp else 14.dp)
            ) {
                Image(
                    painter = painterResource(id = opponent.greetingImageRes()),
                    contentDescription = opponent.displayName(),
                    modifier = Modifier
                        .size(imageSize)
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    ComicSpeechBubble(
                        text = opponent.greetingText(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (nightMode) gameTheme.primaryColor else MichiButton,
                            contentColor = if (nightMode) gameTheme.backgroundColor else MichiWhite
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.super_greeting_continue),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun CatOpponent.greetingImageRes(): Int {
    return when (this) {
        CatOpponent.LILY -> R.drawable.hi_lily
        CatOpponent.COCO -> R.drawable.hi_coco
        CatOpponent.SALEM -> R.drawable.hi_salem
    }
}

@Composable
private fun CatOpponent.greetingText(): String {
    return when (this) {
        CatOpponent.LILY -> stringResource(R.string.super_greeting_lily)
        CatOpponent.COCO -> stringResource(R.string.super_greeting_coco)
        CatOpponent.SALEM -> stringResource(R.string.super_greeting_salem)
    }
}

@Composable
private fun SuperSidePanel(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    onPauseClick: () -> Unit,
    onSettingsClick: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameHeader(gameTheme)
        Spacer(modifier = Modifier.height(if (compact) 12.dp else 20.dp))
        SuperMatchHeader(uiState = uiState, gameTheme = gameTheme, compact = compact)
        Spacer(modifier = Modifier.height(if (compact) 12.dp else 20.dp))
        SuperStatus(uiState = uiState, gameTheme = gameTheme)
        Spacer(modifier = Modifier.height(if (compact) 12.dp else 20.dp))
        GameActions(
            gameTheme = gameTheme,
            onPauseClick = onPauseClick,
            onSettingsClick = onSettingsClick
        )
    }
}

@Composable
private fun SuperMatchHeader(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    compact: Boolean
) {
    val themedMode = gameTheme.themeType != ThemeType.Luz
    val avatarSize = if (compact) 50.dp else 66.dp
    val vsSize = if (compact) 18.sp else 22.sp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (themedMode) gameTheme.boardColor else MichiWhite,
            shadowElevation = 3.dp
        ) {
            Text(
                text = stringResource(R.string.you_short),
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                color = if (themedMode) gameTheme.pauseAccentColor else MichiButton,
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 15.sp else 18.sp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.vs),
            fontFamily = MichiFont,
            fontSize = vsSize,
            color = if (themedMode) gameTheme.pauseAccentColor.copy(alpha = 0.82f) else MichiButton
        )
        Spacer(modifier = Modifier.width(10.dp))
        CatAvatar(
            name = uiState.opponent.displayName(),
            opponent = uiState.opponent,
            modifier = Modifier.size(avatarSize)
        )
    }
}

@Composable
private fun SuperStatus(uiState: GameUiState, gameTheme: MichiGameTheme) {
    val activeBoard = uiState.superGato.activeBoard
    Text(
        text = when {
            uiState.isAiThinking -> stringResource(
                R.string.super_status_thinking,
                uiState.opponent.displayName()
            )

            activeBoard == null -> stringResource(R.string.super_status_any_board)
            else -> stringResource(R.string.super_status_board, activeBoard + 1)
        },
        color = gameTheme.primaryColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SuperGatoBoard(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    onCellTap: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        color = gameTheme.boardColor,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (boardRow in 0 until 3) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (boardCol in 0 until 3) {
                        val boardIndex = boardRow * 3 + boardCol
                        SmallSuperBoard(
                            uiState = uiState,
                            gameTheme = gameTheme,
                            boardIndex = boardIndex,
                            onCellTap = onCellTap,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallSuperBoard(
    uiState: GameUiState,
    gameTheme: MichiGameTheme,
    boardIndex: Int,
    onCellTap: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val owner = uiState.superGato.boards[boardIndex]
    val playable = owner == Player.NONE &&
            !uiState.superGato.fullBoards[boardIndex] &&
            (uiState.superGato.activeBoard == null || uiState.superGato.activeBoard == boardIndex) &&
            !uiState.isAiThinking
    val highlighted = uiState.superGato.activeBoard == boardIndex

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = if (highlighted) 2.dp else 1.dp,
                color = if (highlighted) gameTheme.primaryColor else gameTheme.secondaryColor,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = if (playable) gameTheme.secondaryColor else gameTheme.boardColor
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        for (col in 0 until 3) {
                            val cellIndex = row * 3 + col
                            MiniSuperCell(
                                value = uiState.superGato.cells[boardIndex * 9 + cellIndex],
                                gameTheme = gameTheme,
                                playable = playable,
                                onClick = { onCellTap(boardIndex, cellIndex) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            if (owner != Player.NONE) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gameTheme.boardColor.copy(alpha = 0.76f)),
                    contentAlignment = Alignment.Center
                ) {
                    PlayerMark(
                        value = owner,
                        gameTheme = gameTheme,
                        modifier = Modifier.fillMaxSize(0.75f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniSuperCell(
    value: Player,
    gameTheme: MichiGameTheme,
    playable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .clickable(enabled = playable && value == Player.NONE) { onClick() },
        color = gameTheme.backgroundColor,
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            PlayerMark(
                value = value,
                gameTheme = gameTheme,
                modifier = Modifier.fillMaxSize(0.78f)
            )
        }
    }
}

@Composable
private fun PlayerMark(
    value: Player,
    gameTheme: MichiGameTheme,
    modifier: Modifier = Modifier
) {
    when (value) {
        Player.X -> Icon(
            painter = painterResource(id = gameTheme.xIcon),
            contentDescription = stringResource(R.string.cd_player_x),
            modifier = modifier,
            tint = gameTheme.xColor
        )

        Player.O -> {
            if (gameTheme.useLibraryPawForO) {
                LuzPawBadge(
                    contentDescription = stringResource(R.string.cd_player_o),
                    modifier = modifier,
                    pawColor = gameTheme.oColor
                )
            } else {
                Icon(
                    painter = painterResource(id = gameTheme.oIcon),
                    contentDescription = stringResource(R.string.cd_player_o),
                    modifier = modifier,
                    tint = gameTheme.oColor
                )
            }
        }

        Player.NONE -> {}
    }
}

@Composable
private fun LuzPawBadge(
    contentDescription: String,
    modifier: Modifier = Modifier,
    pawColor: Color
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MichiWhite.copy(alpha = 0.86f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Pets,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(0.72f),
            tint = pawColor
        )
    }
}
