package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times

@Composable
fun ACClearVisibilityToggle(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    if (isVisible) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(20.dp)
        ) {
            ACIconSmall(FontAwesomeIcons.Solid.Times, null)

        }
    }
}