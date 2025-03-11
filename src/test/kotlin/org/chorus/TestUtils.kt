package org.chorus

import org.chorus.level.PlayerChunkManager
import org.chorus.math.Vector3
import org.chorus.utils.GameLoop
import java.lang.reflect.InvocationTargetException

object TestUtils {
    fun serverTick(server: Server?) {
        try {
            val tick = Server::class.java.getDeclaredMethod("tick")
            tick.isAccessible = true
            tick.invoke(server)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }

    fun setField(clazz: Class<*>, target: Any?, fieldName: String, value: Any?) {
        try {
            val infoF = clazz.getDeclaredField(fieldName)
            infoF.isAccessible = true
            infoF[target] = value
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
    }

    fun gameLoop0(p: TestPlayer): GameLoop {
        val loop = GameLoop.builder().loopCountPerSec(100).onTick { d: GameLoop ->
            try {
                p.level.scheduler.mainThreadHeartbeat(d.tick)
                Server.instance.network.process()
                p.level.subTick(d)
                p.checkNetwork()
            } catch (ignore: Exception) {
            }
        }.build()
        val thread = Thread { loop.startLoop() }
        thread.start()
        return loop
    }

    fun resetPlayerStatus(player: TestPlayer) {
        player.level = GameMockExtension.Companion.level!!
        player.setPosition(Vector3(0.0, 100.0, 0.0))
        player.getPlayerChunkManager().usedChunks.clear()
        player.getPlayerChunkManager().inRadiusChunks.clear()
        setField(
            PlayerChunkManager::class.java,
            player.getPlayerChunkManager(),
            "lastLoaderChunkPosHashed",
            Long.MAX_VALUE
        )
    }
}
