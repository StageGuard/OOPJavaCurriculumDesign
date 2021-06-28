package me.stageguard.oopcd.frontend.desktop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.*
import me.stageguard.oopcd.frontend.desktop.Either
import me.stageguard.oopcd.frontend.desktop.core.RandomChineseNameGenerator
import me.stageguard.oopcd.frontend.desktop.core.RollManager
import me.stageguard.oopcd.frontend.desktop.core.SettingField
import me.stageguard.oopcd.frontend.desktop.core.dto.request.ImportStudentDTO
import me.stageguard.oopcd.frontend.desktop.core.dto.request.ImportStudentsDTO
import me.stageguard.oopcd.frontend.desktop.ui.AbstractChildrenComponent
import me.stageguard.oopcd.frontend.desktop.ui.SingleItemLazyColumn
import java.io.File
import java.nio.charset.Charset
import java.text.DecimalFormat
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView


class SettingView(
    ctx: ComponentContext,
    private val onBackPressed: () -> Unit
) : AbstractChildrenComponent(ctx) {
    private var settings by mutableStateOf(SettingFieldInComposeField())
    private var applyState by mutableStateOf("")

    @Composable
    override fun render() {
        BoxWithConstraints(modifier = Modifier.padding(20.dp)) {
            SingleItemLazyColumn {
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
                    value = settings.layer,
                    singleLine = true,
                    label = { Text(text = "分层层数") },
                    onValueChange = {
                        try {
                            settings = settings.copy(layer = it)
                        } catch (ex: Exception) {
                        }
                    },
                    isError = kotlin.run {
                        try {
                            val num = settings.layer.toInt()
                            num == 1
                        } catch (ex: Exception) {
                            true
                        }
                    }.also {
                        settings.layerStatus = !it
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
                    value = settings.ratio,
                    singleLine = true,
                    label = { Text(text = "层数正确率") },
                    onValueChange = {
                        try {
                            settings = settings.copy(ratio = it)
                        } catch (ex: Exception) {
                        }
                    },
                    isError = kotlin.run {
                        try {
                            val list = settings.ratio.split(",").map { it.toDouble() }
                            if (list.count() != settings.layer.toInt()) return@run true
                            list.forEachIndexed { index, d ->
                                if (d >= list.getOrElse(index + 1) { 1.0 }) return@run true
                            }
                            return@run false
                        } catch (ex: Exception) {
                            true
                        }
                    }.also {
                        settings.ratioStatus = !it
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
                    value = settings.transferCount,
                    singleLine = true,
                    label = { Text(text = "错误回答上限") },
                    onValueChange = {
                        try {
                            settings = settings.copy(transferCount = it)
                        } catch (ex: Exception) {
                        }
                    },
                    isError = kotlin.run {
                        try {
                            val list = settings.transferCount.split(",").map {
                                it.toInt().also { n -> if (n <= 0) return@run true }
                            }
                            if (list.count() != settings.layer.toInt()) return@run true
                            return@run false
                        } catch (ex: Exception) {
                            true
                        }
                    }.also {
                        settings.transferCountStatus = !it
                    }
                )
                Spacer(Modifier.height(15.dp))

                Text(
                    """
                隔层抽取，指定抽取时是否从当前层数
                后的层抽取学生，比如当前层数为2，
                总层数是4，那么抽取时将从2,3,4层抽取
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
            Column(modifier = Modifier.padding(50.dp).align(Alignment.TopEnd)) {
                Button(
                    onClick = ::checkAndApplySettings,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("应用设置", fontSize = 18.sp)
                }
                Spacer(Modifier.height(15.dp))
                Button(
                    onClick = ::generateRandomDataAndImport,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("批量新建", fontSize = 18.sp)
                }
                Spacer(Modifier.height(15.dp))
                Button(
                    onClick = ::exportData,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("导出数据", fontSize = 18.sp)
                }
                Spacer(Modifier.height(15.dp))
                Button(
                    onClick = onBackPressed,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("返回", fontSize = 18.sp)
                }
                Spacer(Modifier.height(15.dp))
                Text(applyState, fontSize = 15.sp, textAlign = TextAlign.Justify)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun checkAndApplySettings() {
        val illegalArguments = mutableListOf<String>()
        if (!settings.layerStatus) illegalArguments.add("分层层数")
        if (!settings.ratioStatus) illegalArguments.add("层数正确率")
        if (!settings.transferCountStatus) illegalArguments.add("错误回答上限")
        if (illegalArguments.isNotEmpty()) {
            applyState = "无法应用设置：\n${illegalArguments.joinToString(", ") { it }} 不符合要求。"
            return
        }
        SettingField.layer = settings.layer.toInt()
        SettingField.ratio = settings.ratio.split(",").map { it.toDouble() }
        SettingField.transferCount = settings.transferCount.split(",").map { it.toInt() }
        SettingField.rollAlsoFromNextLayer = settings.rollAlsoFromNextLayer
        applyState = "应用成功"
        GlobalScope.launch {
            delay(3000L)
            applyState = ""
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun generateRandomDataAndImport() {
        GlobalScope.launch {
            val students = mutableListOf<ImportStudentDTO>()
            for (a in 1..100) {
                students.add(
                    ImportStudentDTO(
                        name = RandomChineseNameGenerator.generateName(),
                        id = RandomChineseNameGenerator.random.nextLong(202003000000, 202003099999),
                        clazz = "20-" + RandomChineseNameGenerator.random.nextInt(5)
                    )
                )
            }
            applyState = when (RollManager.importStudents(ImportStudentsDTO(students))) {
                is Either.Left -> "导入成功"
                is Either.Right -> "导入失败"
            }
            delay(3000L)
            applyState = ""
        }
    }

    @OptIn(DelicateCoroutinesApi::class, ExperimentalComposeUiApi::class)
    private fun exportData() {
        GlobalScope.launch {
            when (val data = RollManager.getStudents()) {
                is Either.Left -> {
                    val dialog = openFileChooser()
                    val result = dialog.showOpenDialog(dialog)
                    if (result == JFileChooser.APPROVE_OPTION) {
                        val filePath: String = dialog.selectedFile.absolutePath
                        val file = File(filePath + File.separator + "export.txt")
                        with(Dispatchers.IO) {
                            file.delete()
                            file.createNewFile()
                            file.appendText(buildString {
                                append("学号\t\t班级\t\t姓名\t\t总回答数\t\t正确回答数\t\t正确率\n")
                                val decimalFormat = DecimalFormat("#######0.00")
                                data.value.students.joinToString("\n") {
                                    "${it.id}\t\t${it.clazz}\t\t${it.name}\t\t${it.totalAnswered}\t\t${it.rightAnswered}\t\t${
                                        if (it.totalAnswered == 0) 0 else decimalFormat.format(it.rightAnswered.toFloat() / it.totalAnswered.toFloat())
                                    }"
                                }.also { append(it) }
                            }, Charset.forName("UTF-8"))
                            applyState = "成功导出到 $file"
                        }

                    }
                }
                is Either.Right -> {
                    applyState = "导出失败：" + data.value.error
                }
            }
            delay(3000L)
            applyState = ""
        }
    }

    private fun openFileChooser() = JFileChooser().apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        currentDirectory = FileSystemView.getFileSystemView().homeDirectory
        dialogTitle = "导出至..."
        approveButtonText = "导出"
    }

    private data class SettingFieldInComposeField(
        val layer: String = SettingField.layer.toString(),
        var layerStatus: Boolean = true,
        val ratio: String = SettingField.ratio.joinToString(",") { it.toString() },
        var ratioStatus: Boolean = true,
        val transferCount: String = SettingField.transferCount.joinToString(",") { it.toString() },
        var transferCountStatus: Boolean = true,
        val rollAlsoFromNextLayer: Boolean = SettingField.rollAlsoFromNextLayer
    )
}