package io.github.ppoonk.ac.ui.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.CheckSquare


@Composable
fun ACDropdownMenu(
    selected: String,
    list: List<ACDropdownMenuItem>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
//    properties: PopupProperties = DefaultMenuProperties,
//    shape: Shape = MenuDefaults.shape,
    shape: Shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    containerColor: Color = MenuDefaults.containerColor,
    tonalElevation: Dp = MenuDefaults.TonalElevation,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
//    content: @Composable() (ColumnScope.() -> Unit)
) {
    val internalModifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.then(internalModifier),
        offset = offset,
        scrollState = scrollState,
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,

        ) {
        list.forEachIndexed { index, ACDropdownMenuItem ->
            DropdownMenuItem(
                leadingIcon = {
                    if (selected == ACDropdownMenuItem.text) ACIconSmall(
                        FontAwesomeIcons.Solid.CheckSquare,
                        contentDescription = null
                    )
                },
                trailingIcon = { Icon(ACDropdownMenuItem.trailingIcon, contentDescription = null) },
                text = { Text(ACDropdownMenuItem.text) },
                onClick = ACDropdownMenuItem.onClick
            )
            if ((list.size - 1 - index) > 0) HorizontalDivider()
        }
    }
}

data class ACDropdownMenuItem(
    val text: String,
    val trailingIcon: ImageVector,
    val onClick: () -> Unit,
)