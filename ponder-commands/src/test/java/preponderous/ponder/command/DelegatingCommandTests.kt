package preponderous.ponder.command

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import preponderous.ponder.command.result.CommandFailure
import preponderous.ponder.command.result.CommandSuccess
import preponderous.ponder.command.result.IncorrectUsageFailure

class DelegatingCommandTests {

    private val usageMessage = "Example usage message"

    private fun senderMock() = mockk<CommandSender> {
        every { sendMessage(any()) } just runs
    }

    @Test
    fun `delegating command delegates to subcommand`() = runBlocking {
        val subcommand = mockk<Command> {
            coEvery { execute(any(), any()) } returns CommandSuccess
        }
        val sender = mockk<CommandSender> {
            every { sendMessage(any()) } just runs
        }
        val underTest = DelegatingCommand(
            mapOf(
                "test" to subcommand
            ),
            "Example usage message"
        )
        underTest.execute(sender, "test", "test2")
        coVerify { subcommand.execute(sender, "test2") }
    }

    @Test
    fun `subcommand result is returned to the caller`() = runBlocking {
        val sender = senderMock()
        val underTest = DelegatingCommand(mapOf("test" to Command { _, _ -> CommandSuccess }), usageMessage)
        assertSame(CommandSuccess, underTest.execute(sender, "test"))
        verify(exactly = 0) { sender.sendMessage(any()) }
    }

    @Test
    fun `subcommand named alone receives no arguments`() = runBlocking {
        var received: Array<out String>? = null
        val subcommand = Command { _, args ->
            received = args
            CommandSuccess
        }
        val underTest = DelegatingCommand(mapOf("test" to subcommand), usageMessage)
        underTest.execute(senderMock(), "test")
        assertArrayEquals(emptyArray<String>(), received)
    }

    @Test
    fun `empty arguments send the usage message and fail`() = runBlocking {
        val sender = senderMock()
        val underTest = DelegatingCommand(mapOf("test" to Command { _, _ -> CommandSuccess }), usageMessage)
        val result = underTest.execute(sender)
        assertTrue(result is IncorrectUsageFailure)
        verify { sender.sendMessage(usageMessage) }
    }

    @Test
    fun `unknown subcommand sends the usage message and fails`() = runBlocking {
        val sender = senderMock()
        val subcommand = mockk<Command>()
        val underTest = DelegatingCommand(mapOf("test" to subcommand), usageMessage)
        val result = underTest.execute(sender, "absent")
        assertTrue(result is IncorrectUsageFailure)
        verify { sender.sendMessage(usageMessage) }
        coVerify(exactly = 0) { subcommand.execute(any(), *anyVararg()) }
    }

    @Test
    fun `subcommand added after construction is routed to`() = runBlocking {
        val underTest = DelegatingCommand(emptyMap(), usageMessage)
        val subcommand = Command { _, _ -> CommandSuccess }
        underTest.addCommand("test", subcommand)
        assertSame(subcommand, underTest.getCommand("test"))
        assertSame(CommandSuccess, underTest.execute(senderMock(), "test"))
    }

    @Test
    fun `subcommands given at construction are copied`() {
        val subcommands = mutableMapOf<String, Command>("test" to Command { _, _ -> CommandSuccess })
        val underTest = DelegatingCommand(subcommands, usageMessage)
        subcommands.clear()
        assertNotNull(underTest.getCommand("test"))
    }

    @Test
    fun `getCommand returns null for an unregistered name`() {
        val underTest = DelegatingCommand(mapOf("test" to Command { _, _ -> CommandSuccess }), usageMessage)
        assertNull(underTest.getCommand("absent"))
    }

    @Test
    fun `subcommand failure is returned without sending the usage message`() = runBlocking {
        val sender = senderMock()
        val failure = CommandFailure()
        val underTest = DelegatingCommand(mapOf("test" to Command { _, _ -> failure }), usageMessage)
        assertSame(failure, underTest.execute(sender, "test"))
        verify(exactly = 0) { sender.sendMessage(any()) }
    }

    @Test
    fun `adding a subcommand under an existing name replaces it`() = runBlocking {
        val original = mockk<Command>()
        val replacement = Command { _, _ -> CommandSuccess }
        val underTest = DelegatingCommand(mapOf("test" to original), usageMessage)
        underTest.addCommand("test", replacement)
        assertSame(replacement, underTest.getCommand("test"))
        assertSame(CommandSuccess, underTest.execute(senderMock(), "test"))
        coVerify(exactly = 0) { original.execute(any(), *anyVararg()) }
    }

    @Test
    fun `subcommand names are matched case-sensitively`() = runBlocking {
        val sender = senderMock()
        val underTest = DelegatingCommand(mapOf("test" to Command { _, _ -> CommandSuccess }), usageMessage)
        assertTrue(underTest.execute(sender, "TEST") is IncorrectUsageFailure)
        verify { sender.sendMessage(usageMessage) }
    }

    @Test
    fun `nested delegating commands strip one argument per level`() = runBlocking {
        var received: Array<out String>? = null
        val leaf = Command { _, args ->
            received = args
            CommandSuccess
        }
        val inner = DelegatingCommand(mapOf("inner" to leaf), "Inner usage message")
        val underTest = DelegatingCommand(mapOf("outer" to inner), usageMessage)
        assertSame(CommandSuccess, underTest.execute(senderMock(), "outer", "inner", "arg"))
        assertArrayEquals(arrayOf("arg"), received)
    }

    @Test
    fun `nested delegating command sends its own usage message on an unknown subcommand`() = runBlocking {
        val sender = senderMock()
        val inner = DelegatingCommand(mapOf("inner" to Command { _, _ -> CommandSuccess }), "Inner usage message")
        val underTest = DelegatingCommand(mapOf("outer" to inner), usageMessage)
        assertTrue(underTest.execute(sender, "outer", "absent") is IncorrectUsageFailure)
        verify(exactly = 1) { sender.sendMessage("Inner usage message") }
        verify(exactly = 0) { sender.sendMessage(usageMessage) }
    }

    @Test
    fun `remaining arguments are passed through in order`() = runBlocking {
        var received: Array<out String>? = null
        val subcommand = Command { _, args ->
            received = args
            CommandSuccess
        }
        val underTest = DelegatingCommand(mapOf("test" to subcommand), usageMessage)
        underTest.execute(senderMock(), "test", "first", "second", "third")
        assertArrayEquals(arrayOf("first", "second", "third"), received)
    }

    @Test
    fun `quoted arguments are passed through without unquoting`() = runBlocking {
        var received: Array<out String>? = null
        val subcommand = Command { _, args ->
            received = args
            CommandSuccess
        }
        val underTest = DelegatingCommand(mapOf("test" to subcommand), usageMessage)
        underTest.execute(senderMock(), "test", "\"hello", "world\"", "plain")
        assertArrayEquals(arrayOf("\"hello", "world\"", "plain"), received)
    }

    @Test
    fun `a quoted subcommand name is not unquoted before lookup`() = runBlocking {
        val sender = senderMock()
        val subcommand = mockk<Command>()
        val underTest = DelegatingCommand(mapOf("test" to subcommand), usageMessage)
        assertTrue(underTest.execute(sender, "\"test\"") is IncorrectUsageFailure)
        verify(exactly = 1) { sender.sendMessage(usageMessage) }
        coVerify(exactly = 0) { subcommand.execute(any(), *anyVararg()) }
    }

}
