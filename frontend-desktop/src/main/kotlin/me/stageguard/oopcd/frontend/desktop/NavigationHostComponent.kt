package me.stageguard.oopcd.frontend.desktop

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router
import me.stageguard.oopcd.frontend.desktop.components.FlashView
import me.stageguard.oopcd.frontend.desktop.components.RollView

class NavigationHostComponent(
    ctx: ComponentContext
) : Component, ComponentContext by ctx {

    private val navigationRouter = router<ChildrenStates, AbstractChildrenComponent>(
        initialConfiguration = ChildrenStates.FlashView
    ) { (states, context) -> createChildFactory(states, context) }

    private fun createChildFactory(
        states: ChildrenStates,
        context: ComponentContext
    ) = when (states) {
        is ChildrenStates.FlashView -> FlashView(context)
        is ChildrenStates.RollView -> RollView(context)
    }

    @Composable
    override fun render() {
        Children(routerState = navigationRouter.state) {

        }
    }
}