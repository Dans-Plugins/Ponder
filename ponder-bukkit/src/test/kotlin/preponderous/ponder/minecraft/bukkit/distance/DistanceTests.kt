package preponderous.ponder.minecraft.bukkit.distance

import io.mockk.every
import io.mockk.mockk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import preponderous.ponder.minecraft.bukkit.distance.blocks
import preponderous.ponder.minecraft.bukkit.distance.findPlayersWithin
import preponderous.ponder.minecraft.bukkit.distance.of
import preponderous.ponder.minecraft.bukkit.distance.within
import java.util.function.Predicate

class DistanceTests {

    @Test
    fun `location within distance returns true when target location is within distance`() {
        val world = mockk<World>()
        assertTrue(Location(world, 1.0, 1.0, 1.0) within 16.blocks of Location(world, 17.0, 1.0, 1.0))
    }

    @Test
    fun `location within distance returns false when target location is outside of distance`() {
        val world = mockk<World>()
        assertFalse(Location(world, 1.0, 1.0, 1.0) within 16.blocks of Location(world, 1.0, 1.0, 17.1))
    }

    @Test
    fun `player within distance returns true when target player is within distance`() {
        val world = mockk<World>()
        val player = mockk<Player>()
        every { player.location } returns Location(world, 1.0, 1.0, 1.0)
        val target = mockk<Player>()
        every { target.location } returns Location(world, 17.0, 1.0, 1.0)
        assertTrue(player within 16.blocks of target)
    }

    @Test
    fun `player within distance returns false when target player is outside of distance`() {
        val world = mockk<World>()
        val player = mockk<Player>()
        every { player.location } returns Location(world, 1.0, 1.0, 1.0)
        val target = mockk<Player>()
        every { target.location } returns Location(world, 1.0, 17.1, 1.0)
        assertFalse(player within 16.blocks of target)
    }

    @Test
    fun `findPlayersWithin distance returns players within distance only`() {
        val world1 = mockk<World>()
        val player1 = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 0.0)
            every { world } returns world1
        }
        val player2 = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 16.0)
        }
        val player3 = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 16.1)
        }
        val player4 = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 8.0)
        }
        every { world1.players } returns listOf(player1, player2, player3, player4)
        assertEquals(player1.findPlayersWithin(16.blocks), listOf(player2, player4))
    }

    @Test
    fun `Int blocks wraps the value as a Distance`() {
        assertEquals(16.0, 16.blocks.value)
    }

    @Test
    fun `Double blocks wraps the value as a Distance`() {
        assertEquals(2.5, 2.5.blocks.value)
    }

    @Test
    fun `valueSquared is the value multiplied by itself`() {
        assertEquals(6.25, 2.5.blocks.valueSquared)
    }

    @Test
    fun `location exactly at the distance is within it`() {
        val world = mockk<World>()
        assertTrue(Location(world, 0.0, 0.0, 0.0) within 5.blocks of Location(world, 3.0, 4.0, 0.0))
    }

    @Test
    fun `location is within zero blocks of itself`() {
        val world = mockk<World>()
        assertTrue(Location(world, 1.0, 2.0, 3.0) within 0.blocks of Location(world, 1.0, 2.0, 3.0))
    }

    @Test
    fun `location in another world cannot be measured`() {
        val world1 = mockk<World>(relaxed = true)
        val world2 = mockk<World>(relaxed = true)
        assertThrows(IllegalArgumentException::class.java) {
            Location(world1, 0.0, 0.0, 0.0) within 16.blocks of Location(world2, 0.0, 0.0, 0.0)
        }
    }

    @Test
    fun `of applies the predicate to the value`() {
        val isEven = Predicate<Int> { it % 2 == 0 }
        assertTrue(isEven of 4)
        assertFalse(isEven of 3)
    }

    @Test
    fun `findPlayersWithin returns an empty list when the receiver is alone in the world`() {
        val world1 = mockk<World>()
        val player1 = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 0.0)
            every { world } returns world1
        }
        every { world1.players } returns listOf(player1)
        assertEquals(emptyList<Player>(), player1.findPlayersWithin(16.blocks))
    }

    @Test
    fun `Int blocks and Double blocks of the same value are equal`() {
        assertEquals(16.0.blocks, 16.blocks)
    }

    @Test
    fun `negative distance behaves like its magnitude`() {
        val world = mockk<World>()
        assertEquals(25.0, (-5).blocks.valueSquared)
        assertTrue(Location(world, 0.0, 0.0, 0.0) within (-5).blocks of Location(world, 3.0, 4.0, 0.0))
        assertFalse(Location(world, 0.0, 0.0, 0.0) within (-5).blocks of Location(world, 3.0, 4.1, 0.0))
    }

    @Test
    fun `NaN distance matches no location, not even the origin itself`() {
        val world = mockk<World>()
        assertFalse(Location(world, 0.0, 0.0, 0.0) within Double.NaN.blocks of Location(world, 0.0, 0.0, 0.0))
    }

    @Test
    fun `location predicate can be reused against several targets`() {
        val world = mockk<World>()
        val nearOrigin = Location(world, 0.0, 0.0, 0.0) within 10.blocks
        assertTrue(nearOrigin of Location(world, 0.0, 10.0, 0.0))
        assertFalse(nearOrigin of Location(world, 0.0, 10.1, 0.0))
        assertTrue(nearOrigin of Location(world, -6.0, 0.0, 8.0))
    }

    @Test
    fun `location predicate reads the origin when tested, not when created`() {
        val world = mockk<World>()
        val origin = Location(world, 0.0, 0.0, 0.0)
        val nearOrigin = origin within 5.blocks
        val target = Location(world, 20.0, 0.0, 0.0)
        assertFalse(nearOrigin of target)
        origin.x = 18.0
        assertTrue(nearOrigin of target)
    }

    @Test
    fun `player predicate reads the player's location when tested, not when created`() {
        val world = mockk<World>()
        val player = mockk<Player>()
        every { player.location } returns Location(world, 0.0, 0.0, 0.0)
        val nearPlayer = player within 5.blocks
        val target = mockk<Player>()
        every { target.location } returns Location(world, 20.0, 0.0, 0.0)
        assertFalse(nearPlayer of target)
        every { player.location } returns Location(world, 18.0, 0.0, 0.0)
        assertTrue(nearPlayer of target)
    }

    @Test
    fun `player in another world cannot be measured`() {
        val world1 = mockk<World>(relaxed = true)
        val world2 = mockk<World>(relaxed = true)
        val player = mockk<Player>()
        every { player.location } returns Location(world1, 0.0, 0.0, 0.0)
        val target = mockk<Player>()
        every { target.location } returns Location(world2, 0.0, 0.0, 0.0)
        assertThrows(IllegalArgumentException::class.java) {
            player within 16.blocks of target
        }
    }

    @Test
    fun `findPlayersWithin keeps the world's player order`() {
        val world1 = mockk<World>()
        val receiver = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 0.0)
            every { world } returns world1
        }
        val far = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 9.0)
        }
        val near = mockk<Player> {
            every { location } returns Location(world1, 0.0, 0.0, 1.0)
        }
        every { world1.players } returns listOf(far, receiver, near)
        assertEquals(listOf(far, near), receiver.findPlayersWithin(10.blocks))
    }

    @Test
    fun `findPlayersWithin includes another player at the receiver's exact location`() {
        val world1 = mockk<World>()
        val receiver = mockk<Player> {
            every { location } returns Location(world1, 4.0, 5.0, 6.0)
            every { world } returns world1
        }
        val stacked = mockk<Player> {
            every { location } returns Location(world1, 4.0, 5.0, 6.0)
        }
        every { world1.players } returns listOf(receiver, stacked)
        assertEquals(listOf(stacked), receiver.findPlayersWithin(0.blocks))
    }

}