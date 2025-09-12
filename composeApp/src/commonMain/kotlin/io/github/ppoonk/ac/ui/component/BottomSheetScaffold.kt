package io.github.ppoonk.ac.ui.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACBottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
//    sheetPeekHeight: Dp = BottomSheetDefaults.SheetPeekHeight,
    sheetPeekHeight: Dp = 0.dp,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    sheetShape: Shape = BottomSheetDefaults.ExpandedShape,
//    sheetContainerColor: Color = BottomSheetDefaults.ContainerColor,
    sheetContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    sheetContentColor: Color = contentColorFor(sheetContainerColor),
    sheetTonalElevation: Dp = 0.dp,
    sheetShadowElevation: Dp = BottomSheetDefaults.Elevation,
    sheetDragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    sheetSwipeEnabled: Boolean = true,
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = sheetContent,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                scope.launch {
                    if (scaffoldState.bottomSheetState.isVisible) {
                        scaffoldState.bottomSheetState.hide()
                    }
                }
            })
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetMaxWidth = sheetMaxWidth,
        sheetShape = sheetShape,
        sheetContainerColor = sheetContainerColor,
        sheetContentColor = sheetContentColor,
        sheetTonalElevation = sheetTonalElevation,
        sheetShadowElevation = sheetShadowElevation,
        sheetDragHandle = sheetDragHandle,
        sheetSwipeEnabled = sheetSwipeEnabled,
        topBar = topBar,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}

