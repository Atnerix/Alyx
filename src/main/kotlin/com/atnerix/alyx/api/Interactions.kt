package com.atnerix.alyx.api

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.interactions.modals.ModalInteraction

data class SlashInteraction(val slashInteraction: SlashCommandInteraction, val channel: MessageChannelUnion)

data class CommandModalInteraction(val interaction: ModalInteraction)

data class OptionData(
    val optionType: OptionType,
    val name: String,
    val description: String = "",
    val required: Boolean = false,
    val autoComplete: Boolean = false
)