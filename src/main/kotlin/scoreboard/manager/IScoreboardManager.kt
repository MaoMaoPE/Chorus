package org.chorus_oss.chorus.scoreboard.manager

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.entity.EntityLiving
import org.chorus_oss.chorus.scoreboard.IScoreboard
import org.chorus_oss.chorus.scoreboard.data.DisplaySlot
import org.chorus_oss.chorus.scoreboard.displayer.IScoreboardViewer
import org.chorus_oss.chorus.scoreboard.storage.IScoreboardStorage

/**
 * 管理，储存一批计分板 <br></br>
 * 此接口面向/scoreboard命令，若只是想要显示信息，请直接操作scoreboard对象
 */
interface IScoreboardManager {
    /**
     * 添加一个计分板
     * @param scoreboard 目标计分板
     * @return 是否添加成功（返回false若计分板已存在或者事件被撤销）
     */
    fun addScoreboard(scoreboard: IScoreboard): Boolean

    /**
     * 删除一个计分板
     * @param scoreboard 目标计分板
     * @return 是否删除成功（返回false若计分板已存在或者事件被撤销）
     */
    fun removeScoreboard(scoreboard: IScoreboard): Boolean

    /**
     * 删除一个计分板
     * @param objectiveName 目标计分板标识名称
     * @return 是否删除成功（返回false若计分板已存在或者事件被撤销）
     */
    fun removeScoreboard(objectiveName: String): Boolean

    /**
     * 获取计分板对象（若存在）
     * @param objectiveName 目标计分板标识名称
     * @return 计分板对象
     */
    fun getScoreboard(objectiveName: String?): IScoreboard?

    /**
     * 获取所有计分板对象
     * @return 所有计分板对象
     */
    val scoreboards: MutableMap<String, IScoreboard>

    /**
     * 检查是否存在指定计分板
     * @param scoreboard 指定计分板
     * @return 是否存在
     */
    fun containScoreboard(scoreboard: IScoreboard): Boolean

    /**
     * 检查是否存在指定计分板
     * @param name 指定计分板标识名称
     * @return 是否存在
     */
    fun containScoreboard(name: String?): Boolean

    /**
     * 获取显示槽位信息
     * @return 显示槽位信息
     */
    val display: MutableMap<DisplaySlot, IScoreboard?>

    /**
     * 获取指定显示槽位的计分板（若存在）
     * @param slot 指定槽位
     * @return 计分板对象
     */
    fun getDisplaySlot(slot: DisplaySlot): IScoreboard?

    /**
     * 设置指定槽位显示计分板
     * 若形参scoreboard为null,则清除指定槽位内容
     * @param slot 指定槽位
     * @param scoreboard 计分板对象
     */
    fun setDisplay(slot: DisplaySlot, scoreboard: IScoreboard?)

    /**
     * 获取所有观察者
     * @return 所有观察者
     */
    val viewers: Set<IScoreboardViewer?>?

    /**
     * 添加一个观察者
     * @param viewer 目标观察者
     * @return 是否添加成功
     */
    fun addViewer(viewer: IScoreboardViewer): Boolean

    /**
     * 删除一个观察者（若存在）
     * @param viewer 目标观察者
     * @return 是否删除成功
     */
    fun removeViewer(viewer: IScoreboardViewer): Boolean

    /**
     * 服务端内部方法
     */
    fun onPlayerJoin(player: Player)

    /**
     * 服务端内部方法
     */
    fun beforePlayerQuit(player: Player)

    /**
     * 服务端内部方法
     */
    fun onEntityDead(entity: EntityLiving)

    /**
     * 获取计分板存储器实例
     * @return 存储器实例
     */
    val storage: IScoreboardStorage?

    /**
     * 通过存储器保存计分板信息
     */
    fun save()

    /**
     * 从存储器重新读取计分板信息
     */
    fun read()
}
