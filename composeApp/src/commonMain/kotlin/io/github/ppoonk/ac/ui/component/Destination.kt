package io.github.ppoonk.ac.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable


@Stable
class DestinationState(
    initialValue: ACDestination
) {
    private var _currentDestination = mutableStateOf(initialValue)

    /** 当前选中的目的地 */
    val currentDestination: ACDestination
        get() = _currentDestination.value

    /**
     * 跳转到指定目的地
     * @param destination 目标目的地
     */
    suspend fun to(destination: ACDestination) {
        _currentDestination.value = destination
    }

    companion object {
        fun Saver() = Saver<DestinationState, ACDestination>(
            save = { it.currentDestination },
            restore = { DestinationState(it) }
        )
    }
}

/**
 * 创建并记住DestinationState实例
 * @param initialValue 初始目的地，默认为首页
 * @return DestinationState实例
 */
@Composable
fun rememberDestinationState(
    initialValue: ACDestination
): DestinationState {
    return rememberSaveable(saver = DestinationState.Saver()) {
        DestinationState(initialValue)
    }
}