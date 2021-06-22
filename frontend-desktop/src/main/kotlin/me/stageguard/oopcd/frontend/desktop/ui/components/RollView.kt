package me.stageguard.oopcd.frontend.desktop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.stageguard.oopcd.frontend.desktop.Either
import me.stageguard.oopcd.frontend.desktop.core.RollManager
import me.stageguard.oopcd.frontend.desktop.core.dto.response.StudentInfoDTO
import me.stageguard.oopcd.frontend.desktop.ui.AbstractChildrenComponent

class RollView(
    ctx: ComponentContext,
    val onSettingButtonPressed: () -> Unit
) : AbstractChildrenComponent(ctx) {

    private var rollSession by mutableStateOf<String?>(null)
    private var students = listOf<StudentInfoDTO>()
    private var rolledStudent by mutableStateOf<StudentInfoDTO?>(null)
    private var rolled by mutableStateOf(false)
    private var isRollClickable by mutableStateOf(true)
    private var isAnswerClickable by mutableStateOf(false)

    private var errorMessage by mutableStateOf("")

    @Composable
    override fun render() {
        BoxWithConstraints(
            modifier = Modifier.padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = buildAnnotatedString {
                        append("姓名：")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 35.sp
                            )
                        ) {
                            append(rolledStudent?.name ?: "")
                        }
                    },
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.width(500.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = buildAnnotatedString {
                        append("学号：")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 35.sp
                            )
                        ) {
                            append(if (rolledStudent?.id == null) "" else rolledStudent?.id.toString())
                        }
                    },
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.width(500.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { answer(isRight = false) },
                        enabled = isAnswerClickable
                    ) {
                        Text(
                            text = "错误",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false, radius = 80.dp),
                            onClick = ::startRoll, enabled = isRollClickable
                        ).background(
                            Brush.linearGradient(
                                0.0f to Color(21, 153, 87),
                                1.0f to Color(21, 87, 153)
                            ), CircleShape
                        ).size(140.dp),
                    ) {
                        Text(
                            text = "点名",
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    Button(
                        onClick = { answer(isRight = true) },
                        enabled = isAnswerClickable
                    ) {
                        Text(
                            text = "正确",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 25.dp),
                    onClick = onSettingButtonPressed
                ).size(30.dp).align(Alignment.BottomEnd),
            ) {
                Image(
                    painter = svgResource("setting.svg"),
                    contentDescription = "参数设置",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startRoll() {
        GlobalScope.launch {
            if (rollSession == null) {
                isRollClickable = false
                when (val rollSessionDTO = RollManager.createRollSession()) {
                    is Either.Left -> rollSession = rollSessionDTO.value.sessionKey
                    is Either.Right -> {
                        errorMessage = rollSessionDTO.value.error
                        clearStatus(true)
                        return@launch
                    }
                }
            }
            when (val studentsInfoDTO = RollManager.getStudents()) {
                is Either.Left -> students = studentsInfoDTO.value.students
                is Either.Right -> {
                    errorMessage = studentsInfoDTO.value.error
                    clearStatus(true)
                    return@launch
                }
            }
            when (val rollDTO = RollManager.roll(sessionKey = rollSession!!)) {
                is Either.Left -> rolledStudent = rollDTO.value.student
                is Either.Right -> {
                    errorMessage = rollDTO.value.error
                    clearStatus(true)
                    return@launch
                }
            }
            rollAnimation()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun answer(isRight: Boolean) {
        GlobalScope.launch {
            if (rollSession != null) {
                when (val rollDTO = RollManager.answer(sessionKey = rollSession!!, isRight)) {
                    is Either.Left -> {
                    }
                    is Either.Right -> {
                        errorMessage = rollDTO.value.error
                        clearStatus(true)
                        return@launch
                    }
                }
                clearStatus(clearRollSession = isRight)
            } else {
                errorMessage = "Error: rollSession is null, roll to unrolled state."
                clearStatus(true)
            }
        }
    }

    private fun clearStatus(clearRollSession: Boolean) {
        rolled = false
        rolledStudent = null
        if (clearRollSession) rollSession = null
        students = listOf()
        isRollClickable = true
        isAnswerClickable = false
    }

    private suspend fun rollAnimation() {
        errorMessage = ""
        isRollClickable = false
        var delayTime = 25
        var rollTimes = 35
        val targetStudent = rolledStudent!!.copy()
        while (rollTimes-- > 0) {
            rolledStudent = students.random()
            delay(delayTime.toLong())
            delayTime += 5
        }
        rolledStudent = targetStudent
        isAnswerClickable = true
    }
}