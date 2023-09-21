package com.atnerix.alyx.api

interface SlashedCommand {
    fun invokeCommand(interaction: SlashInteraction)

    fun getName(): String

    fun getDescription(): String = ""

    fun hasModal(): Boolean = false

    fun getModal(): CommandModalInteraction? = null

    fun getOptions(): MutableList<OptionData> = mutableListOf()
}