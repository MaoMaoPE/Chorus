package org.chorus.command.selector.args.impl

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.exceptions.SelectorSyntaxException
import org.chorus.entity.Entity
import org.chorus.level.Transform
import java.util.function.Predicate

class LM : CachedSimpleSelectorArgument() {
    @Throws(SelectorSyntaxException::class)
    override fun cache(
        selectorType: SelectorType?,
        sender: CommandSender?,
        basePos: Transform?,
        vararg arguments: String
    ): Predicate<Entity> {
        ParseUtils.singleArgument(arguments, keyName)
        ParseUtils.cannotReversed(arguments[0])
        val lm = arguments[0].toInt()
        return Predicate { entity: Entity -> entity is Player && entity.experienceLevel >= lm }
    }

    val keyName: String
        get() = "lm"

    val priority: Int
        get() = 3
}
