package org.chorus.level.particle

import org.chorus.math.Vector3

class CloudParticle @JvmOverloads constructor(pos: Vector3, scale: Int = 0) :
    GenericParticle(pos, TYPE_EVAPORATION, scale)
