package com.luigidev.michixo.mobile.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luigidev.michixo.mobile.presentation.theme.MichiFont
import com.luigidev.michixo.mobile.presentation.theme.MichiSoftBrown
import com.luigidev.michixo.mobile.presentation.theme.MichiWhite

@Composable
fun ComicSpeechBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .padding(start = 14.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = MichiWhite,
            shadowElevation = 4.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                color = MichiSoftBrown,
                fontFamily = MichiFont,
                fontSize = 18.sp,
                lineHeight = 22.sp
            )
        }

        Surface(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterStart),
            shape = comicSpeechTailShape(),
            color = MichiWhite,
            shadowElevation = 2.dp
        ) {}
    }
}

private fun comicSpeechTailShape() = GenericShape { size, _ ->
    moveTo(0f, size.height / 2f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    close()
}
