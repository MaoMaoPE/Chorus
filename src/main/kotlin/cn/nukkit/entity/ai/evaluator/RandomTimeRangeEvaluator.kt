package cn.nukkit.entity.ai.evaluator

import cn.nukkit.entity.mob.EntityMob
import lombok.Getter
import java.util.concurrent.ThreadLocalRandom

@Getter
class RandomTimeRangeEvaluator(//gt
    protected var minTime: Int, protected var maxTime: Int
) : IBehaviorEvaluator {
    protected var nextTargetTime: Int = -1

    override fun evaluate(entity: EntityMob): Boolean {
        if (this.nextTargetTime == -1) {
            this.updateNextTargetTime(entity)
            return false
        }
        val currentTime = entity.level!!.tick
        if (currentTime >= nextTargetTime) {
            this.updateNextTargetTime(entity)
            return true
        } else {
            return false
        }
    }

    protected fun updateNextTargetTime(entity: EntityMob) {
        this.nextTargetTime = entity.level!!.tick + ThreadLocalRandom.current().nextInt(minTime, maxTime + 1)
    }
}
