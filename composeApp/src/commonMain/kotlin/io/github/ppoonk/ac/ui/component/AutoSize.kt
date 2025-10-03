package io.github.ppoonk.ac.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass


enum class WindowSize { Compact, Medium, Expanded }

@Composable
private fun AutoSize(
    size: WindowSize,
    compact: @Composable () -> Unit = {},
    medium: @Composable () -> Unit = {},
    expanded: @Composable () -> Unit = {}
): Unit {
    when (size) {
        WindowSize.Compact -> compact()
        WindowSize.Medium -> medium()
        WindowSize.Expanded -> expanded()
    }
}

@Composable
fun AutoSizeFade(
    compact: @Composable () -> Unit = {},
    medium: @Composable () -> Unit = {},
    expanded: @Composable () -> Unit = {}
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val size = when {
        // 扩展宽度（如平板横屏、桌面端）
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> WindowSize.Expanded
        // 中等宽度（如平板竖屏、大屏手机横屏）
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> WindowSize.Medium
        // 紧凑宽度（如手机竖屏）：单列布局
        else -> WindowSize.Compact
    }

    Crossfade(
        targetState = size,
        animationSpec = tween(durationMillis = 800),
    ) { size ->
        AutoSize(size, compact, medium, expanded)
    }
}

@Composable
fun AutoSizeAnimated(
    compact: @Composable () -> Unit = {},
    medium: @Composable () -> Unit = {},
    expanded: @Composable () -> Unit = {}
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val size = when {
        // 扩展宽度（如平板横屏、桌面端）
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> WindowSize.Expanded
        // 中等宽度（如平板竖屏、大屏手机横屏）
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> WindowSize.Medium
        // 紧凑宽度（如手机竖屏）：单列布局
        else -> WindowSize.Compact
    }
    AnimatedContent(
        targetState = size,
        transitionSpec = {
            // 滑动进入 + 淡出
            slideInHorizontally(
                animationSpec = tween(800), initialOffsetX = { width -> width })
                .togetherWith(fadeOut())
        }
    ) { size ->
        AutoSize(size, compact, medium, expanded)
    }
}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ACBoxWithConstraints(
    modifier: Modifier = Modifier,
    compact: @Composable () -> Unit,
    medium: @Composable () -> Unit,
    expanded: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        val windowSize = when {
            maxWidth < 600.dp -> WindowSize.Compact
            maxWidth < 840.dp -> WindowSize.Medium
            else -> WindowSize.Expanded
        }

        AnimatedContent(
            targetState = windowSize,
            transitionSpec = {
                // 滑动进入 + 淡出
                slideInHorizontally(
                    animationSpec = tween(800), initialOffsetX = { width -> width })
                    .togetherWith(fadeOut())
            }
        ) { targetWindowSize ->
            when (targetWindowSize) {
                WindowSize.Compact -> compact()
                WindowSize.Medium -> medium()
                WindowSize.Expanded -> expanded()
            }
        }
    }
}