package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun ACCard(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
//    colors: CardColors = CardDefaults.cardColors(),
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ),
    elevation: CardElevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
    ),
    border: BorderStroke? = null,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier.padding(2.dp).alpha(if (enabled) 1f else 0.6f),
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content
    )
}