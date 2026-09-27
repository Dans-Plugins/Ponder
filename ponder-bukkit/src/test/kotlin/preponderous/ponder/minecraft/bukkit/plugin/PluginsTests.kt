package preponderous.ponder.minecraft.bukkit.plugin

import io.mockk.*
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.junit.jupiter.api.Test
import preponderous.ponder.minecraft.bukkit.plugin.registerListeners

class PluginsTests {

    @Test
    fun `registerListeners registers listeners with the plugin manager`() {
        val pluginManager = mockk<PluginManager> {
            every { registerEvents(any(), any()) } just runs
        }
        val plugin = mockk<Plugin> {
            every { server } returns mockk server@{
                every { this@server.pluginManager } returns pluginManager
            }
        }
        val listener1 = mockk<Listener>()
        val listener2 = mockk<Listener>()
        plugin.registerListeners(listener1, listener2)
        verify {
            pluginManager.registerEvents(listener1, plugin)
            pluginManager.registerEvents(listener2, plugin)
        }
    }

    @Test
    fun `registerListeners with no listeners registers nothing`() {
        val pluginManager = mockk<PluginManager>()
        val plugin = mockk<Plugin> {
            every { server } returns mockk server@{
                every { this@server.pluginManager } returns pluginManager
            }
        }
        plugin.registerListeners()
        verify(exactly = 0) { pluginManager.registerEvents(any(), any()) }
    }

}