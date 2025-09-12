package io.github.ppoonk.ac.ui.component

import io.github.ppoonk.ac.LocalDrawerState
import io.github.ppoonk.ac.LocalNavController
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.AngleLeft
import compose.icons.fontawesomeicons.solid.Bars
import kotlinx.coroutines.launch


@Composable
 fun DefaultNavigationIconForBack(): Unit {
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    IconButton(
        onClick = {
            scope.launch {
                // 返回页面
                navController.popBackStack()
            }
        }) { ACIcon(FontAwesomeIcons.Solid.AngleLeft, null) }

}
@Composable
 fun DefaultNavigationIconForDrawerOpen(): Unit {
    val scope = rememberCoroutineScope()
    val drawerState = LocalDrawerState.current
    IconButton(
        onClick = {
            scope.launch {
                drawerState.open()
            }
        }) { ACIcon(FontAwesomeIcons.Solid.Bars, null) }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ACTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = { DefaultNavigationIconForBack() },
    actions: @Composable() (RowScope.() -> Unit) = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
//    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    colors: TopAppBarColors = TopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        actionIconContentColor = MaterialTheme.colorScheme.onBackground
    ),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}