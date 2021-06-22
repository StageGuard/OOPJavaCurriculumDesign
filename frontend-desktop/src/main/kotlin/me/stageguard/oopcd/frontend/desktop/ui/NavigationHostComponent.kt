package me.stageguard.oopcd.frontend.desktop.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.popWhile
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import me.stageguard.oopcd.frontend.desktop.ui.components.FlashView
import me.stageguard.oopcd.frontend.desktop.ui.components.RollView
import me.stageguard.oopcd.frontend.desktop.ui.components.SettingView

class NavigationHostComponent(
    ctx: ComponentContext
) : AbstractChildrenComponent(ctx) {

    private val navigationRouter = router<ChildrenStates, AbstractChildrenComponent>(
        initialConfiguration = ChildrenStates.RollView
    ) { state, context ->
        when (state) {
            is ChildrenStates.FlashView -> FlashView(context)
            is ChildrenStates.RollView -> RollView(context, ::switchToSettingPage)
            is ChildrenStates.SettingView -> SettingView(context, ::switchBackFromSettingPage)
        }
    }

    private fun switchToSettingPage() {
        navigationRouter.push(ChildrenStates.SettingView)
    }

    private fun switchBackFromSettingPage() {
        navigationRouter.popWhile { it is ChildrenStates.SettingView }
    }

    @Composable
    override fun render() {
        Children(routerState = navigationRouter.state) {
            it.instance.render()
        }
    }
}