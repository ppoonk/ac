package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ACSingleChoiceSegmentedButtonRow(
    selected: String,
    list: List<SegmentedButtonItem>,
    modifier: Modifier = Modifier,
    space: Dp = SegmentedButtonDefaults.BorderWidth,
//    content: @Composable() (SingleChoiceSegmentedButtonRowScope.() -> Unit)
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
        space = space,
    ) {
        list.forEachIndexed { index, segmentedButtonItem ->
            SegmentedButton(
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp
                    )

                    list.size - 1 -> RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    else -> RectangleShape
                },
                colors=SegmentedButtonDefaults.colors().copy(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(0.dp, Color.Transparent),
                onClick = {
                    segmentedButtonItem.onClick()
                },
                selected = segmentedButtonItem.label == selected,
                label = {
                    Text(
                        segmentedButtonItem.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}


data class SegmentedButtonItem(
    val label: String,
    val onClick: () -> Unit,
)