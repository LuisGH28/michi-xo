package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.presentation.CatOpponent
import com.luigidev.michixo.mobile.presentation.theme.MichiPink
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown

@Composable
fun CatAvatar(
    name: String,
    opponent: CatOpponent?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRes = remember(opponent) {
        val resourceName = when (opponent) {
            CatOpponent.LILY -> "cat_lily"
            CatOpponent.COCO -> "cat_coco"
            CatOpponent.SALEM -> "cat_salem"
            null -> null
        }

        resourceName?.let {
            context.resources.getIdentifier(it, "drawable", context.packageName)
        } ?: 0
    }
    val background = when (opponent) {
        CatOpponent.LILY -> colorResource(R.color.michi_lily_cream)
        CatOpponent.COCO -> colorResource(R.color.michi_coco_board)
        CatOpponent.SALEM -> colorResource(R.color.michi_salem_night)
        null -> MichiPink
    }
    val tint = when (opponent) {
        CatOpponent.SALEM -> colorResource(R.color.michi_salem_moon)
        CatOpponent.COCO -> colorResource(R.color.michi_coco_accent)
        CatOpponent.LILY -> colorResource(R.color.michi_lily_accent)
        null -> MichiSoftBrown
    }

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = background,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(0.54f),
                    tint = tint
                )
            }
        }
    }
}

@Composable
fun CatOpponent.displayName(): String {
    return when (this) {
        CatOpponent.LILY -> stringResource(R.string.opponent_lily)
        CatOpponent.COCO -> stringResource(R.string.opponent_coco)
        CatOpponent.SALEM -> stringResource(R.string.opponent_salem)
    }
}

@Composable
fun CatOpponent.description(): String {
    return when (this) {
        CatOpponent.LILY -> stringResource(R.string.opponent_lily_desc)
        CatOpponent.COCO -> stringResource(R.string.opponent_coco_desc)
        CatOpponent.SALEM -> stringResource(R.string.opponent_salem_desc)
    }
}
