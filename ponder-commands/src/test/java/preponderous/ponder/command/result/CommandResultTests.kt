package preponderous.ponder.command.result

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CommandResultTests {

    private fun describe(result: CommandResult) = when (result) {
        is CommandSuccess -> "success"
        is CommandFailure -> "failure"
    }

    @Test
    fun `CommandSuccess is a single shared instance`() {
        val first: CommandResult = CommandSuccess
        val second: CommandResult = CommandSuccess
        assertSame(first, second)
    }

    @Test
    fun `IncorrectUsageFailure is a CommandFailure`() {
        val result: CommandResult = IncorrectUsageFailure()
        assertTrue(result is CommandFailure)
    }

    @Test
    fun `a CommandFailure subclass is handled by the CommandFailure branch`() {
        class CustomFailure : CommandFailure()
        assertEquals("failure", describe(CustomFailure()))
        assertEquals("failure", describe(IncorrectUsageFailure()))
        assertEquals("failure", describe(CommandFailure()))
        assertEquals("success", describe(CommandSuccess))
    }

    @Test
    fun `failures are compared by identity, not by type`() {
        val first = IncorrectUsageFailure()
        val second = IncorrectUsageFailure()
        assertNotSame(first, second)
        assertNotEquals(first, second)
        assertNotEquals(CommandFailure(), CommandFailure())
    }

}
