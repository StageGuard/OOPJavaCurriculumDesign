package me.stageguard.oopcd.frontend.desktop.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import com.arkivanov.decompose.ComponentContext
import me.stageguard.oopcd.frontend.desktop.AbstractChildrenComponent

class RollView(
    ctx: ComponentContext
) : AbstractChildrenComponent(ctx) {
    override fun render() {
        Column {
            Text("114514")
        }
    }
}