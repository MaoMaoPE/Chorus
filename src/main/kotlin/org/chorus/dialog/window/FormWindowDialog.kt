package org.chorus.dialog.window

import com.google.common.reflect.TypeToken
import org.chorus.Player
import org.chorus.dialog.element.ElementDialogButton
import org.chorus.dialog.handler.FormDialogHandler
import org.chorus.entity.Entity
import org.chorus.utils.JSONUtils
import org.chorus.utils.Loggable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.stream.Collectors


class FormWindowDialog @JvmOverloads constructor(
    @JvmField var title: String?,
    @JvmField var content: String?,
    @JvmField var bindEntity: Entity?,
    private var buttons: MutableList<ElementDialogButton> = mutableListOf()
) :
    Dialog {
    @JvmField
    var skinData: String = ""

    // Please do not call this method at will, otherwise it may cause potential bugs
    // usually you shouldn't edit this
    // but here this value is used to be an identifier
    var sceneName: String = (dialogId++).toString()
        // Please do not call this method at will, otherwise it may cause potential bugs
        protected set

    @Transient
    protected val handlers: MutableList<FormDialogHandler> = mutableListOf()

    init {
        try {
            BufferedReader(InputStreamReader(Objects.requireNonNull(javaClass.classLoader.getResourceAsStream("npc_data.json")))).use { reader ->
                this.skinData = reader.lines().collect(
                    Collectors.joining("\n")
                )
            }
        } catch (e: IOException) {
            FormWindowDialog.log.error("Failed to load npc_data.json: ", e)
        }

        requireNotNull(this.bindEntity) { "bindEntity cannot be null!" }
    }

    fun getButtons(): List<ElementDialogButton> {
        return buttons
    }

    fun setButtons(buttons: MutableList<ElementDialogButton>) {
        this.buttons = buttons
    }

    fun addButton(text: String) {
        this.addButton(ElementDialogButton(text, text))
    }

    fun addButton(button: ElementDialogButton) {
        buttons.add(button)
    }

    val entityId: Long
        get() = bindEntity!!.getRuntimeID()

    fun addHandler(handler: FormDialogHandler) {
        handlers.add(handler)
    }

    fun getHandlers(): List<FormDialogHandler> {
        return handlers
    }

    var buttonJSONData: String?
        get() = JSONUtils.to(this.buttons)
        set(json) {
            val buttons = JSONUtils.from<MutableList<ElementDialogButton>?>(json, object : TypeToken<List<ElementDialogButton>?>() {}.type) ?: mutableListOf()
            this.setButtons(buttons)
        }

    fun updateSceneName() {
        this.sceneName = (dialogId++).toString()
    }

    override fun send(player: Player) {
        player.showDialogWindow(this)
    }

    companion object : Loggable {
        private var dialogId: Long = 0
    }
}
