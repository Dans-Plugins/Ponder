package preponderous.ponder.command

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
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
        assertTrue(underTest.getCommand("test") != null)
    }

    @Test
    fun `getCommand returns null for an unregistered name`() {
        val underTest = DelegatingCommand(mapOf("test" to Command { _, _ -> CommandSuccess }), usageMessage)
        assertNull(underTest.getCommand("absent"))
    }

}
