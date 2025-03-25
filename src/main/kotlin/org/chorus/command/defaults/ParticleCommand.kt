package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.level.Locator
import org.chorus.level.ParticleEffect
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class ParticleCommand(name: String) : VanillaCommand(name, "commands.particle.description") {
    init {
        this.permission = "chorus.command.particle"
        commandParameters.clear()
        val particles =  ParticleEffect.entries.map { it.identifier }
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newEnum("effect", CommandEnum("particle", particles, true)),
            CommandParameter.Companion.newType("position", CommandParamType.POSITION),
            CommandParameter.Companion.newType("count", true, CommandParamType.INT)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val name = result.value.getResult<String>(0)!!
        val locator = result.value.getResult<Locator>(1)!!
        var count = 1
        if (result.value.hasResult(2)) count = result.value.getResult(2)!!
        if (count < 1) {
            log.addNumTooSmall(2, 1).output()
            return 0
        }
        for (i in 0..<count) {
            locator.level.addParticleEffect(
                locator.position.asVector3f(),
                name,
                -1,
                locator.level.dimension
            )
        }
        log.addSuccess("commands.particle.success", name, count.toString())
        return 1
    }
}
