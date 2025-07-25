package org.chorus_oss.chorus.event

/**
 * 所有的监听事件的类必须实现的接口。<br></br>
 * An interface implemented by all classes that handles events.
 *
 *
 * 插件要监听事件，需要一个类实现这个接口，在这个类里编写方法来监听。这个类称作**监听类**。
 * 监听类中监听事件的方法称作事件的**处理器**。一个监听类可以包含多个不同的事件处理器。
 * 实现监听类后，插件需要在插件管理器中注册这个监听类。<br></br>
 * If a plugin need to listen events, there must be a class implement this interface. This class is called a **listener class**.
 * Methods with specified parameters should be written in order to listen events. This method is called a **handler**.
 * One listener class could contain many different handlers.
 * After implemented the listener class, plugin should register it in plugin level.
 *
 *
 * 事件监听器被注册后，Nukkit会在需要监听的事件发生时，使用反射来调用监听类中对应的处理器。<br></br>
 * After registered, Nukkit will call the handler in the listener classes by reflection when a event happens.
 *
 *
 * 这是一个编写监听类和处理器的例子。注意的是，标签`@EventHandler`和参数的类型是必需的：<br></br>
 * Here is an example for writing a listener class and a handler method.
 * Note that for the handler, tag `@EventHandler` and the parameter is required:
 * <pre>
 * public class ExampleListener implements Listener {
 * `@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)`
 * public void onBlockBreak(BlockBreakEvent event) {
 * String blockId = event.getBlock().getPackId();
 * if (blockID == Block.STONE) {
 * event.getPlayer().sendMessage("Oops, my ExampleListener won't let you break a stone!")
 * event.setCancelled(true);
 * }
 * }
 * }
</pre> *
 *
 *
 * 关于注册监听类，请看：[org.chorus_oss.chorus.plugin.PluginManager.registerEvents].<br></br>
 * For registering listener class, See: [org.chorus_oss.chorus.plugin.PluginManager.registerEvents].
 *
 *
 * 关于处理器的优先级和处理器是否忽略被取消的事件，请看：[EventHandler].<br></br>
 * For the priority of handler and whether the handler ignore cancelled events or not, See: [EventHandler].
 *
 * @see org.chorus_oss.chorus.event.Event
 */
interface Listener 
