package me.stageguard.oopcd.frontend.desktop.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import me.stageguard.oopcd.frontend.desktop.AbstractChildrenComponent
import me.stageguard.oopcd.frontend.desktop.roll.SettingField

class SettingView(
    ctx: ComponentContext
) : AbstractChildrenComponent(ctx) {
    private var settings by mutableStateOf(SettingFieldInComposeField())

    @Composable
    override fun render() {
        BoxWithConstraints(modifier = Modifier.padding(20.dp)) {
            LazyColumn {
                item {
                    Column {
                        Text("设置", fontSize = 40.sp, modifier = Modifier.align(Alignment.Start))
                        Spacer(Modifier.height(20.dp))
                        Text("参数设置", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start))
                        Spacer(Modifier.height(15.dp))

                        Text(
                            """
                        分层层数，将学生按照回答正确率分层
                        在提问时，优先从第一层学生开始提问
                    """.trimIndent(), fontSize = 15.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = settings.layer.first,
                            singleLine = true,
                            label = { Text(text = "分层层数") },
                            onValueChange = {
                                try {
                                    settings = settings.copy(layer = Pair(it, settings.layer.second))
                                } catch (ex: Exception) {
                                }
                            },
                            isError = kotlin.run {
                                try {
                                    val num = settings.layer.first.toInt()
                                    num == 1
                                } catch (ex: Exception) {
                                    true
                                }
                            }.also {
                                settings = settings.copy(layer = Pair(settings.layer.first, !it))
                            }
                        )
                        Spacer(Modifier.height(15.dp))

                        Text(
                            """
                        层数正确率，按照此正确率分界分层
                        数值个数和分层层数相等，从小到大填写
                    """.trimIndent(), fontSize = 15.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = settings.ratio.first,
                            singleLine = true,
                            label = { Text(text = "层数正确率") },
                            onValueChange = {
                                try {
                                    settings = settings.copy(ratio = Pair(it, settings.ratio.second))
                                } catch (ex: Exception) {
                                }
                            },
                            isError = kotlin.run {
                                try {
                                    val list = settings.ratio.first.split(",").map { it.toDouble() }
                                    if (list.count() != settings.layer.first.toInt()) return@run true
                                    list.forEachIndexed { index, d ->
                                        if (d >= list.getOrElse(index + 1) { 1.0 }) return@run true
                                    }
                                    return@run false
                                } catch (ex: Exception) {
                                    true
                                }
                            }.also {
                                settings = settings.copy(ratio = Pair(settings.ratio.first, !it))
                            }
                        )
                        Spacer(Modifier.height(15.dp))

                        Text(
                            """
                        错误回答上限，当此层未答上来的同学
                        超过了这个数值后将转到下一层提问
                    """.trimIndent(), fontSize = 15.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = settings.transferCount.first,
                            singleLine = true,
                            label = { Text(text = "错误回答上限") },
                            onValueChange = {
                                try {
                                    settings = settings.copy(transferCount = Pair(it, settings.transferCount.second))
                                } catch (ex: Exception) {
                                }
                            },
                            isError = kotlin.run {
                                try {
                                    val list = settings.transferCount.first.split(",").map {
                                        it.toInt().also { n -> if (n <= 0) return@run true }
                                    }
                                    if (list.count() != settings.layer.first.toInt()) return@run true
                                    return@run false
                                } catch (ex: Exception) {
                                    true
                                }
                            }.also {
                                settings = settings.copy(transferCount = Pair(settings.transferCount.first, !it))
                            }
                        )
                        Spacer(Modifier.height(15.dp))

                        Text(
                            """
                        隔层抽取，指定抽取时是否从当前层数
                        后的层抽取学生，比如当前层数为2，
                        总层数是4那么抽取时将从2,3,4层抽取
                    """.trimIndent(), fontSize = 15.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Switch(
                            checked = settings.rollAlsoFromNextLayer,
                            onCheckedChange = {
                                settings = settings.copy(rollAlsoFromNextLayer = it)
                            }
                        )
                    }
                }
            }
        }
    }

    private data class SettingFieldInComposeField(
        val layer: Pair<String, Boolean> = SettingField.layer.toString() to true,
        val ratio: Pair<String, Boolean> = SettingField.ratio.joinToString(",") {
            it.toString()
        } to true,
        val transferCount: Pair<String, Boolean> = SettingField.transferCount.joinToString(",") {
            it.toString()
        } to true,
        val rollAlsoFromNextLayer: Boolean = SettingField.rollAlsoFromNextLayer
    )
}