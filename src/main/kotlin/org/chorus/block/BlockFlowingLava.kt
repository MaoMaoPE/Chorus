package org.chorus.block

import org.chorus.Player
import org.chorus.Server.Companion.instance
import org.chorus.block.property.CommonBlockProperties
import org.chorus.entity.Entity
import org.chorus.entity.effect.EffectType
import org.chorus.entity.item.EntityTnt
import org.chorus.event.block.BlockIgniteEvent
import org.chorus.event.entity.EntityCombustByBlockEvent
import org.chorus.event.entity.EntityDamageByBlockEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.item.*
import org.chorus.level.GameRule
import org.chorus.level.Level
import org.chorus.math.*
import org.chorus.math.BlockFace.Companion.fromIndex
import java.util.*
import java.util.concurrent.ThreadLocalRandom


open class BlockFlowingLava @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLiquid(blockstate) {
    override val lightLevel: Int
        get() = 15

    override val name: String
        get() = "Lava"

    override fun onEntityCollide(entity: Entity) {
        entity.highestPosition -= (entity.highestPosition - entity.position.y) * 0.5

        val ev = EntityCombustByBlockEvent(this, entity, 8)
        instance.pluginManager.callEvent(ev)
        if (!ev.isCancelled // Making sure the entity is actually alive and not invulnerable.
            && entity.isAlive()
            && entity.noDamageTicks == 0
        ) {
            entity.setOnFire(ev.duration)
        }

        if (!entity.hasEffect(EffectType.FIRE_RESISTANCE)) {
            entity.attack(EntityDamageByBlockEvent(this, entity, DamageCause.LAVA, 4f))
        }

        super.onEntityCollide(entity)
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        val ret = level.setBlock(this.position, this, true, false)
        level.scheduleUpdate(this, this.tickRate())

        return ret
    }

    override fun onUpdate(type: Int): Int {
        val result = super.onUpdate(type)

        if (type == Level.BLOCK_UPDATE_RANDOM && level.gameRules.getBoolean(GameRule.DO_FIRE_TICK)) {
            val random: Random = ThreadLocalRandom.current()

            val i = random.nextInt(3)

            if (i > 0) {
                for (k in 0..<i) {
                    val v = position.add((random.nextInt(3) - 1).toDouble(), 1.0, (random.nextInt(3) - 1).toDouble())
                    val block = level.getBlock(v)

                    if (block.isAir) {
                        if (this.isSurroundingBlockFlammable(block)) {
                            val e = BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.LAVA)
                            Server.instance.pluginManager.callEvent(e)

                            if (!e.isCancelled) {
                                val fire = get(FIRE)
                                level.setBlock(v, fire, true)
                                level.scheduleUpdate(fire, fire.tickRate())
                                return Level.BLOCK_UPDATE_RANDOM
                            }

                            return 0
                        }
                    } else if (block.isSolid) {
                        return Level.BLOCK_UPDATE_RANDOM
                    }
                }
            } else {
                for (k in 0..2) {
                    val v = position.add((random.nextInt(3) - 1).toDouble(), 0.0, (random.nextInt(3) - 1).toDouble())
                    val block = level.getBlock(v)

                    if (block.up().isAir && block.burnChance > 0) {
                        val e = BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.LAVA)
                        Server.instance.pluginManager.callEvent(e)

                        if (!e.isCancelled) {
                            val fire = get(FIRE)
                            level.setBlock(v, fire, true)
                            level.scheduleUpdate(fire, fire.tickRate())
                        }
                    }
                }
            }
        }

        return result
    }

    protected fun isSurroundingBlockFlammable(block: Block): Boolean {
        for (face in BlockFace.entries) {
            val b = block.getSide(face)
            if (b.burnChance > 0) {
                return true
            }
        }

        return false
    }

    override fun getLiquidWithNewDepth(depth: Int): BlockLiquid {
        return BlockFlowingLava(
            blockState.setPropertyValue(
                Companion.properties,
                CommonBlockProperties.LIQUID_DEPTH.createValue(depth)
            )
        )
    }

    override fun tickRate(): Int {
        if (level.dimension == Level.DIMENSION_NETHER) {
            return 10
        }
        return 30
    }

    override fun getFlowDecayPerBlock(): Int {
        if (level.dimension == Level.DIMENSION_NETHER) {
            return 1
        }
        return 2
    }

    override fun checkForMixing() {
        var colliding: Block? = null
        val down = this.getSide(BlockFace.DOWN)
        for (side in 1..5) { //don't check downwards side
            val blockSide = this.getSide(fromIndex(side))
            if (blockSide is BlockFlowingWater || blockSide.getLevelBlockAtLayer(1) is BlockFlowingWater) {
                colliding = blockSide
                break
            }
            if (down is BlockSoulSoil) {
                if (blockSide is BlockBlueIce) {
                    liquidCollide(this, get(BASALT))
                    return
                }
            }
        }
        if (colliding != null) {
            if (this.liquidDepth == 0) {
                this.liquidCollide(colliding, get(OBSIDIAN))
            } else if (this.liquidDepth <= 4) {
                this.liquidCollide(colliding, get(COBBLESTONE))
            }
        }
    }

    override fun flowIntoBlock(block: Block, newFlowDecay: Int) {
        if (block is BlockFlowingWater) {
            (block as BlockLiquid).liquidCollide(this, get(STONE))
        } else {
            super.flowIntoBlock(block, newFlowDecay)
        }
    }

    override fun addVelocityToEntity(entity: Entity, vector: Vector3?) {
        if (entity !is EntityTnt) {
            super.addVelocityToEntity(entity, vector)
        }
    }

    override val passableBlockFrictionFactor: Double
        get() = 0.3

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.FLOWING_LAVA, CommonBlockProperties.LIQUID_DEPTH)

    }
}
