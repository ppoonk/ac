package io.github.ppoonk.ac.ui.component


import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


abstract class ACDestination {
    abstract val icon: ImageVector?
    abstract val title: String?
    abstract val content: @Composable () -> Unit

}


/**
 * 根据屏幕尺寸自动切换导航模式：
 * - 小屏幕：抽屉式导航 (Drawer)
 * - 中等屏幕：窄导航栏 (Navigation Rail with bottom labels)
 * - 大屏幕：宽导航栏 (Navigation Rail with right labels)
 */
@Composable
fun ACNavigationDrawer(
    drawerState: DrawerState,
    pageState: PagerState,
    destinations: List<ACDestination>,
    topContent: @Composable (() -> Unit),
    bottomContent: @Composable (() -> Unit),

    ): Unit {
    AutoSizeFade(
        compact = {
            Compact(
                drawerState,
                pageState,
                destinations,
                topContent,
                bottomContent
            )
        },
        medium = {
            Medium(
                pageState,
                destinations,
                topContent,
                bottomContent
            )
        },
        expanded = {
            Expanded(
                pageState,
                destinations,
                topContent,
                bottomContent
            )
        }
    )

}


@Composable
private fun Compact(
    drawerState: DrawerState,
    pageState: PagerState,
    destinations: List<ACDestination>,
    topContent: @Composable (() -> Unit),
    bottomContent: @Composable (() -> Unit),
): Unit {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            CompactDrawerContent(
                drawerState,
                pageState,
                destinations,
                topContent,
                bottomContent
            )
        }
    ) {
        VerticalPager(pageState, userScrollEnabled = false) {
            destinations[it].content.invoke()
        }
    }
}

@Composable
private fun CompactDrawerContent(
    drawerState: DrawerState,
    pageState: PagerState,
    destinations: List<ACDestination>,
    topContent: @Composable (() -> Unit),
    bottomContent: @Composable (() -> Unit),
): Unit {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier.widthIn(max = 200.dp).fillMaxHeight()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            // top
            stickyHeader {
                topContent()
            }
            // menu
            itemsIndexed(destinations) { index, d ->
                NavigationDrawerItem(
                    icon = { d.icon?.let { ACIconSmall(it, contentDescription = null) } },
                    label = { d.title?.let { Text(it) } },
                    selected = index == pageState.currentPage,
                    onClick = {
                        scope.launch {
                            // 关闭抽屉
                            drawerState.close()
                            // 跳转页面
                            pageState.animateScrollToPage(
                                page = index,
                                animationSpec = tween(durationMillis = 700)
                            )
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        // bottom
        bottomContent()
    }
}

@Composable
private fun Medium(
    pageState: PagerState,
    destinations: List<ACDestination>,
    topContent: @Composable (() -> Unit),
    bottomContent: @Composable (() -> Unit),
): Unit {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail() {
            // top
            topContent()
            // menu
            destinations.forEachIndexed { index, d ->
                NavigationRailItem(
                    icon = { d.icon?.let { ACIconSmall(it, contentDescription = null) } },
                    label = { d.title?.let { Text(it) } },
                    selected = d == destinations[pageState.currentPage],
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(
                                page = index,
                                animationSpec = tween(durationMillis = 700)
                            )
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // bottom
            bottomContent()
        }
        // 主内容区域
        VerticalPager(
            state = pageState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            destinations[page].content.invoke()
        }
    }
}


@Composable
private fun Expanded(
    pageState: PagerState,
    destinations: List<ACDestination>,
    topContent: @Composable (() -> Unit),
    bottomContent: @Composable (() -> Unit),
): Unit {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(modifier = Modifier.widthIn(max = 200.dp)) {
            topContent()
            destinations.forEachIndexed { index, d ->
                NavigationRailItem(
                    icon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,

                            ) {
                            d.icon?.let { ACIconSmall(it, contentDescription = d.title) }
                            Spacer(Modifier.width(16.dp))
                            d.title?.let { Text(it) }
                        }
                    },
                    selected = d == destinations[pageState.currentPage],
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(
                                page = index,
                                animationSpec = tween(durationMillis = 700)
                            )
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            bottomContent()
        }
        // 主内容区域
        HorizontalPager(
            state = pageState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            destinations[page].content.invoke()
        }
    }

}