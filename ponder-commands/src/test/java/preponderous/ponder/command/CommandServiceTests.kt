package preponderous.ponder.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import preponderous.ponder.command.result.CommandSuccess

class CommandServiceTests {

    @Test
    fun `command can be added`() {
        val underTest: CommandService = DefaultCommandService()
        val command = Command { _, _ -> CommandSuccess }
        underTest.addCommand("test", command)
        assertEquals(command, underTest.getCommand("test"))
    }

    @Test
    fun `getCommand returns null for an unregistered name`() {
        val underTest: CommandService = DefaultCommandService()
        underTest.addCommand("test", Command { _, _ -> CommandSuccess })
        assertNull(underTest.getCommand("absent"))
    }

    @Test
    fun `getCommand returns null before anything is registered`() {
        val underTest: CommandService = DefaultCommandService()
        assertNull(underTest.getCommand("test"))
    }

    @Test
    fun `adding a command under an existing name replaces it`() {
        val underTest: CommandService = DefaultCommandService()
        val replacement = Command { _, _ -> CommandSuccess }
        underTest.addCommand("test", Command { _, _ -> CommandSuccess })
        underTest.addCommand("test", replacement)
        assertSame(replacement, underTest.getCommand("test"))
    }

    @Test
    fun `commands registered under different names are kept apart`() {
        val underTest: CommandService = DefaultCommandService()
        val first = Command { _, _ -> CommandSuccess }
        val second = Command { _, _ -> CommandSuccess }
        underTest.addCommand("first", first)
        underTest.addCommand("second", second)
        assertSame(first, underTest.getCommand("first"))
        assertSame(second, underTest.getCommand("second"))
    }

    @Test
    fun `command names are matched case-sensitively`() {
        val underTest: CommandService = DefaultCommandService()
        underTest.addCommand("test", Command { _, _ -> CommandSuccess })
        assertNull(underTest.getCommand("TEST"))
    }

}
