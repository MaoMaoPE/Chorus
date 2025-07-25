package org.chorus_oss.chorus.network.protocol.types

enum class AuthInputAction {
    ASCEND,
    DESCEND,
    NORTH_JUMP,
    JUMP_DOWN,
    SPRINT_DOWN,
    CHANGE_HEIGHT,
    JUMPING,
    AUTO_JUMPING_IN_WATER,
    SNEAKING,
    SNEAK_DOWN,
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_LEFT,
    UP_RIGHT,
    WANT_UP,
    WANT_DOWN,
    WANT_DOWN_SLOW,
    WANT_UP_SLOW,
    SPRINTING,
    ASCEND_BLOCK,
    DESCEND_BLOCK,
    SNEAK_TOGGLE_DOWN,
    PERSIST_SNEAK,
    START_SPRINTING,
    STOP_SPRINTING,
    START_SNEAKING,
    STOP_SNEAKING,
    START_SWIMMING,
    STOP_SWIMMING,
    START_JUMPING,
    START_GLIDING,
    STOP_GLIDING,
    PERFORM_ITEM_INTERACTION,
    PERFORM_BLOCK_ACTIONS,
    PERFORM_ITEM_STACK_REQUEST,

    /**
     * @since v567
     */
    HANDLE_TELEPORT,

    /**
     * @since v575
     */
    EMOTING,

    /**
     * @since v594
     */
    MISSED_SWING,

    /**
     * @since v594
     */
    START_CRAWLING,

    /**
     * @since v594
     */
    STOP_CRAWLING,

    /**
     * @since v618
     */
    START_FLYING,

    /**
     * @since v618
     */
    STOP_FLYING,

    /**
     * @since v622
     */
    RECEIVED_SERVER_DATA,

    /**
     * @since v649
     */
    IN_CLIENT_PREDICTED_IN_VEHICLE,

    /**
     * @since v662
     */
    PADDLE_LEFT,

    /**
     * @since v662
     */
    PADDLE_RIGHT,

    /**
     * @since v685
     */
    BLOCK_BREAKING_DELAY_ENABLED,

    /**
     * @since v729
     */
    HORIZONTAL_COLLISION,

    /**
     * @since v729
     */
    VERTICAL_COLLISION,

    /**
     * @since v729
     */
    DOWN_LEFT,

    /**
     * @since v729
     */
    DOWN_RIGHT,

    /**
     * @since v748
     */
    START_USING_ITEM,

    /**
     * @since v748
     */
    CAMERA_RELATIVE_MOVEMENT_ENABLED,

    /**
     * @since v748
     */
    ROT_CONTROLLED_BY_MOVE_DIRECTION,

    /**
     * @since v748
     */
    START_SPIN_ATTACK,

    /**
     * @since v748
     */
    STOP_SPIN_ATTACK,

    /**
     * @since v765
     */
    IS_HOTBAR_ONLY_TOUCH,

    /**
     * @since v765
     */
    JUMP_RELEASED_RAW,

    /**
     * @since v765
     */
    JUMP_PRESSED_RAW,

    /**
     * @since v765
     */
    JUMP_CURRENT_RAW,

    /**
     * @since v765
     */
    SNEAK_RELEASED_RAW,

    /**
     * @since v765
     */
    SNEAK_PRESSED_RAW,

    /**
     * @since v765
     */
    SNEAK_CURRENT_RAW;

    companion object {
        private val VALUES = entries.toTypedArray()

        fun from(id: Int): AuthInputAction {
            return VALUES[id]
        }

        fun size(): Int {
            return VALUES.size
        }
    }
}