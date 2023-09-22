package com.atnerix.alyx.api

import com.atnerix.alyx.api.Range.Companion.isNotEmpty
import net.dv8tion.jda.api.interactions.components.ItemComponent
import net.dv8tion.jda.api.interactions.components.LayoutComponent
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.jetbrains.annotations.ApiStatus

interface SlashedCommand {
    fun invokeCommand(interaction: SlashInteraction) {}

    fun getName(): String

    fun getDescription(): String = "Description for this command is not provided"

    fun hasModal(): Boolean = false

    fun invokeModal(modal: CommandModalInteraction) {}

    @ApiStatus.Experimental
    fun getModals(): Map<String, CommandModalInteraction.() -> Unit> = hashMapOf()

    fun getOptions(): ArrayList<OptionData> = arrayListOf() // Static, for cancel other edit

    @ApiStatus.NonExtendable
    fun createModal(
        id: String,
        desc: String = "Description for this modal is not provided",
        vararg components: LayoutComponent
    ): Modal {
        val modal = Modal.create(id, desc)

        modal.addComponents(*components)

        return modal.build()
    }

    @ApiStatus.NonExtendable
    fun createModal(
        id: String,
        desc: String = "Description for this modal is not provided",
        vararg components: ItemComponent
    ): Modal {
        val modal = Modal.create(id, desc)

        modal.addActionRow(*components)

        return modal.build()
    }

    @ApiStatus.NonExtendable
    fun createTextInput(
        id: String,
        name: String,
        style: TextInputStyle = TextInputStyle.SHORT,
        placeholder: String = "",
        range: Range = Range.EMPTY,
        required: Boolean = true
    ): TextInput {
        val textInput = TextInput.create(id, name, style)

        if (placeholder.isNotEmpty()) textInput.setPlaceholder(placeholder)

        if (range.isNotEmpty()) textInput.setRequiredRange(range.min, range.max)

        textInput.setRequired(required)

        return textInput.build()
    }
}