package me.stageguard.oopcd.frontend.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import me.stageguard.oopcd.frontend.desktop.AbstractChildrenComponent

class RollView(
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


            // Spacing between text and button
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 80.dp),
                    onClick = {}
                ).background(
                    Brush.linearGradient(
                        0.0f to Color(21, 153, 87),
                        1.0f to Color(21, 87, 153)
                    ), CircleShape
                ).size(140.dp),
            ) {
                Text(
                    text = "Text",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}