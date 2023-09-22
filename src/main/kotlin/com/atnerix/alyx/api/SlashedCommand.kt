package com.atnerix.alyx.api

interface SlashedCommand {
    fun invokeCommand(interaction: SlashInteraction) {}

    fun getName(): String

    fun getDescription(): String = ""

    fun hasModal(): Boolean = false

    fun invokeModal(modal: CommandModalInteraction) {}

    fun getOptions(): ArrayList<OptionData> = arrayListOf() // Static, for cancel other edit
}