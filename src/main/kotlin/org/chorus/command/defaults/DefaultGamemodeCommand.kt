package org.chorus.command.defaults

import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class DefaultGamemodeCommand(name: String) : VanillaCommand(name, "commands.defaultgamemode.description") {
    init {
        this.permission = "nukkit.command.defaultgamemode"
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newType("gameMode", CommandParamType.INT)
        )
        commandParameters["byString"] = arrayOf(
            CommandParameter.Companion.newEnum("gameMode", CommandEnum.Companion.ENUM_GAMEMODE)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val gameMode: Int
        when (result.key) {
            "default" -> gameMode = list!!.getResult(0)!!
            "byString" -> {
                val mode = list!!.getResult<String>(0)
                gameMode = Server.getGamemodeFromString(mode)
            }

            else -> {
                return 0
            }
        }

        val valid = gameMode >= 0 && gameMode <= 3
        if (valid) {
            Server.instance.properties[ServerPropertiesKeys.GAMEMODE, gameMode]
            log.addSuccess("commands.defaultgamemode.success", Server.getGamemodeString(gameMode)).output()
            return 1
        } else {
            log.addError("commands.gamemode.fail.invalid", gameMode.toString()).output()
            return 0
        }
    }
}
