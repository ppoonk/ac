package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash


@Composable
fun ACPasswordVisibilityToggle(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(20.dp)
    ) {
        ACIconSmall(
            imageVector = if (isVisible) {
                FontAwesomeIcons.Solid.Eye
            } else {
                FontAwesomeIcons.Solid.EyeSlash
            },
            contentDescription =null
        )
    }
}