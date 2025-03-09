package org.chorus.entity.ai.evaluator

import org.chorus.entity.mob.EntityMob
import java.util.concurrent.ThreadLocalRandom

open class ProbabilityEvaluator(protected var probability: Int, protected var total: Int) : IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob?): Boolean {
        return ThreadLocalRandom.current().nextInt(total) < probability
    }
}
