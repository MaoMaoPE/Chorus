package cn.nukkit.level

import cn.nukkit.GameMockExtension
import cn.nukkit.TestPlayer
import cn.nukkit.TestUtils
import cn.nukkit.block.*
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GameMockExtension::class)
class LevelTest {
    @Test
    fun test_getRedstonePower(level: Level) {
        level.setBlockStateAt(
            0, 0, 0, BlockObserver.PROPERTIES.getBlockState(
                CommonBlockProperties.POWERED_BIT.createValue(true),
                CommonBlockProperties.MINECRAFT_FACING_DIRECTION.createValue(BlockFace.WEST) //West is the direction of the observer's face, and the opposite side of the observer's direction can output power
            )
        )
        //For example, when a redstone is in the east of the observer
        level.setBlockStateAt(1, 0, 0, BlockRedstoneWire.PROPERTIES.defaultState)
        //judge if there is a redstone power source in the west of the block
        Assertions.assertEquals(
            15,
            level.getRedstonePower(Vector3(1.0, 0.0, 0.0).getSide(BlockFace.WEST), BlockFace.WEST)
        )
        Assertions.assertEquals(
            0,
            level.getRedstonePower(Vector3(1.0, 0.0, 0.0).getSide(BlockFace.EAST), BlockFace.EAST)
        ) //observer cant output on east

        level.setBlockStateAt(1, 0, 0, BlockOakWood.PROPERTIES.defaultState)
        Assertions.assertEquals(
            15,
            level.getRedstonePower(Vector3(1.0, 0.0, 0.0), BlockFace.WEST)
        ) //wood be strong power with observer
    }

    @Test
    fun test_regenerateChunk(player: TestPlayer) {
        val p = player
        TestUtils.resetPlayerStatus(p)

        p.level.autoSave = true
        p.setPosition(Vector3(0.0, 100.0, 0.0))
        val chunk = p.locator.chunk
        chunk.setBlockState(0, 3, 0, BlockDirt.PROPERTIES.defaultState)
        Assertions.assertEquals(chunk.getBlockState(0, 3, 0), BlockDirt.PROPERTIES.defaultState)
        chunk.setBlockState(0, 3, 0, BlockDiamondBlock.PROPERTIES.defaultState)
        Assertions.assertEquals(chunk.getBlockState(0, 3, 0), BlockDiamondBlock.PROPERTIES.defaultState)

        val gameLoop = TestUtils.gameLoop0(p)
        var limit1 = 100
        while (limit1-- != 0) {
            try {
                if (49 == p.usedChunks.size) {
                    break
                }
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        if (limit1 <= 0) {
            p.level.autoSave = false
            TestUtils.resetPlayerStatus(p)
            Assertions.fail<Any>("Chunk cannot be regenerate in 10s, chunk size " + p.usedChunks.size)
        }
        p.level.regenerateChunk(0, 0)

        var limit = 100
        while (limit-- != 0) {
            try {
                if (p.usedChunks.contains(Level.chunkHash(0, 0))) {
                    break
                }
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
        if (limit <= 0) {
            p.level.autoSave = false
            TestUtils.resetPlayerStatus(p)
            Assertions.fail<Any>("Chunk cannot be regenerate in 10s")
        }

        Assertions.assertEquals(BlockDirt.PROPERTIES.defaultState, p.locator.chunk.getBlockState(0, 3, 0))
        p.level.autoSave = false

        gameLoop.stop()
        TestUtils.resetPlayerStatus(p)
    }
}
