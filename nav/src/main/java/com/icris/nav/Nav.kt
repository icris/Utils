@file:Suppress("unused")

package com.icris.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.channels.Channel


internal sealed interface NavEvent {
    data class Nav(
        val route: String,
        val navOptionsBuilder: NavOptionsBuilder.() -> Unit
    ) : NavEvent
     data object Back : NavEvent
}

object Nav {
    internal val ch = Channel<NavEvent>()
    fun navigate(
        direction: Direction,
        navOptionsBuilder: NavOptionsBuilder.() -> Unit = {
            launchSingleTop = true
            restoreState = true
        }
    ) {
        ch.trySend(NavEvent.Nav(direction.route, navOptionsBuilder))
    }

    fun navigate(
        route: String,
        navOptionsBuilder: NavOptionsBuilder.() -> Unit = {
            launchSingleTop = true
            restoreState = true
        }
    ) {
        ch.trySend(NavEvent.Nav(route, navOptionsBuilder))
    }
    fun back() {
        ch.trySend(NavEvent.Back)

    }
}

infix fun Nav.goto(direction: Direction) = navigate(direction)

@Composable
fun AppNavObserve(navController: NavController) {
    LaunchedEffect(Nav) {
        for (it in Nav.ch) {
            when (it) {
                NavEvent.Back -> navController.popBackStack()
                is NavEvent.Nav -> navController.navigate(it.route, it.navOptionsBuilder)
            }
        }
    }
}