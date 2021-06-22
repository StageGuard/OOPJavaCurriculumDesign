import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.stageguard.oopcd.frontend.desktop.Either
import me.stageguard.oopcd.frontend.desktop.core.RollManager
import org.junit.jupiter.api.Test


class RollManagerTest {
    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun connectionTest() = runBlocking {
        var sessionKey = ""
        when (val result = RollManager.createRollSession()) {
            is Either.Left -> println("Session key: " + result.value.sessionKey.also { sessionKey = it })
            is Either.Right -> print("Error: " + result.value.error)
        }
        delay(750)
        when (val result = RollManager.roll(sessionKey)) {
            is Either.Left -> println("Roll: " + result.value)
            is Either.Right -> print("Error: " + result.value.error)
        }
        delay(750)
        when (val result = RollManager.roll(sessionKey)) {
            is Either.Left -> println("Also Roll: " + result.value)
            is Either.Right -> print("Error: " + result.value.error)
        }
        delay(750)
        when (val result = RollManager.answer(sessionKey, false)) {
            is Either.Left -> println("Answer wrong: " + result.value)
            is Either.Right -> print("Error: " + result.value.error)
        }
        delay(750)
        when (val result = RollManager.roll(sessionKey)) {
            is Either.Left -> println("Roll x2: " + result.value)
            is Either.Right -> print("Error: " + result.value.error)
        }
        delay(750)
        when (val result = RollManager.answer(sessionKey, true)) {
            is Either.Left -> println("AnswerRight: " + result.value)
            is Either.Right -> print("Error: " + result.value.error)
        }
    }
}