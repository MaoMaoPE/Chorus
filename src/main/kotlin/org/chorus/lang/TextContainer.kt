package org.chorus.lang


/**
 * 文本容器
 * 通过[.text]存放文本内容
 *
 * @author MagicDroidX (Nukkit Project)
 */

open class TextContainer(@JvmField var text: String) : Cloneable {
    /**
     * 等于[.getText]
     *
     *
     * equal [.getText]
     */
    override fun toString(): String {
        return this.text
    }

    public override fun clone(): TextContainer {
        return super.clone() as TextContainer
    }
}
