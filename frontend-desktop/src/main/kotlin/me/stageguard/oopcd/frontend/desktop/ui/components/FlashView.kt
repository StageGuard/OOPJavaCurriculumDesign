package me.stageguard.oopcd.frontend.desktop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import me.stageguard.oopcd.frontend.desktop.ui.AbstractChildrenComponent

class FlashView(
    ctx: ComponentContext
) : AbstractChildrenComponent(ctx) {
    @Composable
    override fun render() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Greeting
            Text(
                text = "Roll System",
                fontSize = 40.sp
            )

            // Spacing between text and button
            Spacer(modifier = Modifier.height(30.dp))

            // Go back button
            Text(
                text = "Initializing...",
                fontSize = 20.sp
            )
        }
    }
}