package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.entity.Entity
import org.chorus.level.Locator
import org.chorus.level.Transform
import org.chorus.math.*
import org.chorus.network.protocol.types.PlayerAbility
import kotlin.collections.set

class TeleportCommand(name: String) :
    VanillaCommand(name, "commands.tp.description", "commands.tp.usage", arrayOf<String>("teleport")) {
    init {
        this.permission = "nukkit.command.teleport"
        commandParameters.clear()
        commandParameters["->Entity"] = arrayOf(
            CommandParameter.Companion.newType("destination", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Entity"] = arrayOf(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Pos"] = arrayOf(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newType("yRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newType("xRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Pos(FacingPos)"] = arrayOf(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf("facing")),
            CommandParameter.Companion.newType("lookAtPosition", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["Entity->Pos(FacingEntity)"] = arrayOf(
            CommandParameter.Companion.newType("victim", CommandParamType.TARGET),
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf("facing")),
            CommandParameter.Companion.newType("lookAtEntity", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["->Pos"] = arrayOf(
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newType("yRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newType("xRot", true, CommandParamType.VALUE),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["->Pos(FacingPos)"] = arrayOf(
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf("facing")),
            CommandParameter.Companion.newType("lookAtPosition", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        commandParameters["->Pos(FacingEntity)"] = arrayOf(
            CommandParameter.Companion.newType("destination", CommandParamType.POSITION),
            CommandParameter.Companion.newEnum("facing", false, arrayOf("facing")),
            CommandParameter.Companion.newType("lookAtEntity", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("checkForBlocks", true, CommandEnum.Companion.ENUM_BOOLEAN)
        )
        this.enableParamTree()
    }

    override fun testPermissionSilent(target: CommandSender): Boolean {
        if (target.isPlayer && target.asPlayer()!!.adventureSettings.get(PlayerAbility.TELEPORT)) return true
        return super.testPermissionSilent(target)
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val list = result.value
        when (result.key) {
            "->Entity" -> {
                if (!sender.isEntity) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val destination = list!!.getResult<List<Entity>>(0)!!
                if (destination.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (destination.size > 1) {
                    log.addError("commands.generic.tooManyTargets").output()
                    return 0
                }
                val victim = sender.getTransform()
                val entity = destination[0]
                entity.rotation.yaw = (victim.yaw)
                entity.rotation.pitch = (victim.pitch)
                val target = entity.getTransform()
                var checkForBlocks = false
                if (list.hasResult(1)) {
                    checkForBlocks = list.getResult(1)!!
                }
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess("commands.tp.successVictim", destination[0].getName()).output()
                    } else {
                        log.addError("commands.tp.safeTeleportFail", sender.asEntity()!!.getName(), destination[0].getName())
                            .output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess("commands.tp.successVictim", destination[0].getName()).output()
                }
                return 1
            }

            "Entity->Entity" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val destination = list.getResult<List<Entity>>(1)!!
                if (destination.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (destination.size > 1) {
                    log.addError("commands.generic.tooManyTargets").output()
                    return 0
                }
                val target = destination[0]
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                if (checkForBlocks) {
                    if (!target.getLocator().levelBlock.isSolid && !target.getLocator().add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(
                                target.getTransform().setYaw(victim.rotation.yaw).setPitch(victim.rotation.pitch)
                            )
                        }
                        log.addSuccess("commands.tp.success", sb.toString(), target.getName())
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sb.toString(), target.getName()).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target.getTransform().setYaw(victim.rotation.yaw).setPitch(victim.rotation.pitch))
                    }
                    log.addSuccess("commands.tp.success", sb.toString(), target.getName())
                }
                log.output()
                return victims.size
            }

            "Entity->Pos" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val pos = list.getResult<Locator>(1)
                var yRot = sender.getTransform().rotation.pitch
                if (list.hasResult(2)) {
                    yRot = list.getResult(2)!!
                }
                var xRot = sender.getTransform().rotation.yaw
                if (list.hasResult(3)) {
                    xRot = list.getResult(3)!!
                }
                var checkForBlocks = false
                if (list.hasResult(4)) {
                    checkForBlocks = list.getResult(4)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                val target = Transform.fromObject(pos!!.position, pos.level, xRot, yRot)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(target)
                        }
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sb.toString(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sb.toString(), target.toString()).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target)
                    }
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sb.toString(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                return 1
            }

            "Entity->Pos(FacingPos)" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val pos = list.getResult<Locator>(1)
                val lookAtLocator = list.getResult<Locator>(3)
                var checkForBlocks = false
                if (list.hasResult(4)) {
                    checkForBlocks = list.getResult(4)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                val bv = BVector3.fromPos(
                    Vector3(
                        lookAtLocator!!.position.x - pos!!.position.x,
                        lookAtLocator.position.y - pos.position.y,
                        lookAtLocator.position.z - pos.position.z
                    )
                )
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(target)
                        }
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sb.toString(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError(
                            "commands.tp.safeTeleportFail ",
                            sb.toString(),
                            target.position.floorX.toString() + " " + target.position.floorY + " " + target.position.floorZ
                        ).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target)
                    }
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sb.toString(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "Entity->Pos(FacingEntity)" -> {
                val victims = list!!.getResult<List<Entity>>(0)!!
                if (victims.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                val pos = list.getResult<Locator>(1)
                val lookAtEntity = list.getResult<List<Entity>>(3)!!
                if (lookAtEntity.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (lookAtEntity.size > 1) {
                    log.addTooManyTargets().output()
                    return 0
                }
                val lookAtPosition = lookAtEntity[0].position
                var checkForBlocks = false
                if (list.hasResult(4)) {
                    checkForBlocks = list.getResult(4)!!
                }
                val sb = StringBuilder()
                for (victim in victims) {
                    sb.append(victim.name).append(" ")
                }
                val bv = BVector3.fromPos(lookAtPosition.subtract(pos!!.position))
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        for (victim in victims) {
                            victim.teleport(target)
                        }
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sb.toString(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sb.toString(), target.toString()).output()
                        return 0
                    }
                } else {
                    for (victim in victims) {
                        victim.teleport(target)
                    }
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sb.toString(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "->Pos" -> {
                if (!sender.isEntity) {
                    log.addError("commands.generic.noTargetMatch").output()
                    return 0
                }
                val pos = list.getResult<Locator>(0)
                var yRot = sender.getTransform().rotation.pitch
                if (list.hasResult(1)) {
                    yRot = list.getResult(1)!!
                }
                var xRot = sender.getTransform().rotation.yaw
                if (list.hasResult(2)) {
                    xRot = list.getResult(2)!!
                }
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val target = Transform.fromObject(pos!!.position, pos.level, xRot, yRot)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sender.getName(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sender.getName(), target.toString()).output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sender.getName(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "->Pos(FacingPos)" -> {
                if (!sender.isEntity) {
                    log.addError("commands.generic.noTargetMatch").output()
                    return 0
                }
                val pos = list!!.getResult<Locator>(0)
                val lookAtLocator = list.getResult<Locator>(2)
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val bv = BVector3.fromPos(
                    Vector3(
                        lookAtLocator!!.position.x - pos!!.position.x,
                        lookAtLocator.position.y - pos.position.y,
                        lookAtLocator.position.z - pos.position.z
                    )
                )
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sender.asEntity()!!.getName(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail ", sender.asEntity()!!.getName(), target.toString())
                            .output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sender.asEntity()!!.getName(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            "->Pos(FacingEntity)" -> {
                if (!sender.isEntity) {
                    log.addError("commands.generic.noTargetMatch").output()
                    return 0
                }
                val pos = list!!.getResult<Locator>(0)
                val lookAtEntity = list.getResult<List<Entity>>(2)!!
                if (lookAtEntity.isEmpty()) {
                    log.addNoTargetMatch().output()
                    return 0
                }
                if (lookAtEntity.size > 1) {
                    log.addTooManyTargets().output()
                    return 0
                }
                val lookAtPosition = lookAtEntity[0].position
                var checkForBlocks = false
                if (list.hasResult(3)) {
                    checkForBlocks = list.getResult(3)!!
                }
                val bv = BVector3.fromPos(lookAtPosition.subtract(pos!!.position))
                val target = Transform.fromObject(pos.position, pos.level, bv.yaw, bv.pitch)
                if (checkForBlocks) {
                    if (!target.levelBlock.isSolid && !target.add(0.0, 1.0, 0.0).levelBlock.isSolid) {
                        sender.asEntity()!!.teleport(target)
                        log.addSuccess(
                            "commands.tp.success.coordinates",
                            sender.asEntity()!!.getName(),
                            target.position.floorX.toString(),
                            target.position.floorY.toString(),
                            target.position.floorZ.toString()
                        )
                    } else {
                        log.addError("commands.tp.safeTeleportFail", sender.asEntity()!!.getName(), target.toString())
                            .output()
                        return 0
                    }
                } else {
                    sender.asEntity()!!.teleport(target)
                    log.addSuccess(
                        "commands.tp.success.coordinates",
                        sender.asEntity()!!.getName(),
                        target.position.floorX.toString(),
                        target.position.floorY.toString(),
                        target.position.floorZ.toString()
                    )
                }
                log.output()
                return 1
            }

            else -> {
                return 0
            }
        }
    }
}
