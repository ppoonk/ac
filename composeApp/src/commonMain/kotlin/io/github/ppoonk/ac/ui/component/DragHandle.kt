package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ACDragHandle(
    start: @Composable () -> Unit = {},
    end: @Composable () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        ACHorizontalLine(
            Modifier
                .width(48.dp)
                .height(5.dp)
                .align(Alignment.Center)
        )
        Box(
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            start()
        }
        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            end()
        }
    }
}

@Composable
fun ACHorizontalLine(modifier: Modifier = Modifier): Unit {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(2.dp)
            )
    )
}