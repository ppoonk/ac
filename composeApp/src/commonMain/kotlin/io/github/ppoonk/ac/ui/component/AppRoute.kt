package io.github.ppoonk.ac.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlin.reflect.KClass


@Composable
fun ACAppRoute(
    navController: NavHostController,
    startDestination: Any,
    builder: NavGraphBuilder.() -> Unit
): Unit {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        // 新页面从右侧进入
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(500),
                initialOffsetX = { fullWidth -> fullWidth }) + fadeIn()
        },
        // 旧页面向左侧退出
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(500),
                targetOffsetX = { fullWidth -> -fullWidth }) + fadeOut()
        },
        // 返回时从左侧进入
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(500),
                initialOffsetX = { fullWidth -> -fullWidth }) + fadeIn()
        },
        // 退出时向右侧退出
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(500),
                targetOffsetX = { fullWidth -> fullWidth }) + fadeOut()
        },
        builder = builder
    )
}
