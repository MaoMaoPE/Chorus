package org.chorus_oss.chorus.command

/**
 * 能监听命令执行的类实现的接口。<br></br>
 * An interface what can be implemented by classes which listens command executing.
 *
 * @see org.chorus_oss.chorus.plugin.PluginBase
 *
 * @see org.chorus_oss.chorus.command.CommandExecutor.onCommand
 */
interface CommandExecutor {
    /**
     * 在命令执行时会调用的方法。<br></br>
     * Called when a command is executed.
     *
     *
     * 一个命令可以是`/a_LABEL an_arg1 AN_ARG2...`的形式，这时`label`变量的值为`"a_label"`，
     * `args`数组的元素有`"an_arg1","AN_ARG2",...`。注意到`label`变量会被转化成小写，
     * 而`args`数组内字符串元素的大小写不变。<br></br>
     * A command can be such a form like `/a_LABEL an_arg1 AN_ARG2...`. At this time, the value of
     * variable `label` is `"a_label"`, and the values of elements of array `args` are
     * `"an_arg1","AN_ARG2",...`. Notice that the value of variable `label` will be converted to
     * lower case, but the cases of elements of array `args` won't change.
     *
     *
     * 关于返回值，如果返回`false`，Nukkit会给sender发送这个命令的使用方法等信息，来表示这个命令没有使用成功。
     * 如果你的命令成功的发挥了作用，你应该返回`true`来表示这个命令已执行成功。<br></br>
     * If this function returns `false`, Nukkit will send command usages to command sender, to explain that
     * the command didn't work normally. If your command works properly, a `true` should be returned to explain
     * that the command works.
     *
     *
     * 如果你想测试一个命令发送者是否有权限执行这个命令，
     * 可以使用[org.chorus_oss.chorus.command.Command.testPermissionSilent]。<br></br>
     * If you want to test whether a command sender has the permission to execute a command,
     * you can use [org.chorus_oss.chorus.command.Command.testPermissionSilent].
     *
     * @param sender  这个命令的发送者，可以是玩家或控制台等。<br></br>
     * The sender of this command, this can be a player or a console.
     * @param command 要被发送的命令。<br></br>
     * The command to send.
     * @param label   这个命令的标签。<br></br>
     * Label of the command.
     * @param args    这个命令的参数列表。<br></br>
     * Arguments of this command.
     * @return 这个命令执行是否执行成功。<br></br>whether this command is executed successfully.
     *
     */
    fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String?>?): Boolean
}
