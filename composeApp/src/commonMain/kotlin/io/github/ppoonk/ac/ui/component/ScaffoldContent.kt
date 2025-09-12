package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ACScaffoldContent(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    fadeOutContent: @Composable (Float) -> Unit = {},
    maxFadeOutContentHeight: Dp = 0.dp,
    minFadeOutContentHeight: Dp = 0.dp,
    stickyHeader: @Composable (Float) -> Unit = { },
    scrollContent: @Composable (maxHeight: Dp) -> Unit = {},
) {


    var alpha by remember { mutableFloatStateOf(1f) }
    val maxHeightPx = with(LocalDensity.current) {
        maxFadeOutContentHeight.toPx()
    }
    var currentHeightPx by remember { mutableFloatStateOf(maxHeightPx) }


    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (maxFadeOutContentHeight == 0.dp) return Offset.Zero

                val delta = available.y

                val newHeightPx = currentHeightPx + delta

                currentHeightPx = newHeightPx.coerceIn(0f, maxHeightPx)

                alpha = currentHeightPx / (maxHeightPx)

                return Offset.Zero
            }
        }
    }



    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .nestedScroll(nestedScrollConnection)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(paddingValues)
        ) {
            if (fadeOutContent != {}) {
                item {
                    Row(
                        Modifier
                            .alpha(alpha = alpha),
                    ) {
                        fadeOutContent(alpha)
                    }
                }
            }
            if (fadeOutContent != {}) {
                stickyHeader {
                    stickyHeader(alpha)
                }
            }
            if (scrollContent != {}) {
                item() {
                    scrollContent(maxHeight)
                }
            }
        }
    }


}

