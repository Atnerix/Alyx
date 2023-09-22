package com.atnerix.alyx.api

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.interactions.components.LayoutComponent
import net.dv8tion.jda.api.interactions.modals.ModalInteraction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.jetbrains.annotations.ApiStatus
import java.util.function.Function
import java.util.function.Supplier

data class SlashInteraction(val slash: SlashCommandInteraction, val channel: MessageChannelUnion) {
    val member: Member? = slash.member
    val guild: Guild? = slash.guild
    val self: Member? = guild?.selfMember

    fun deferReply(): ReplyCallbackAction = slash.deferReply()

    fun deferReply(ephemeral: Boolean): ReplyCallbackAction = slash.deferReply(ephemeral)

    fun reply(message: MessageCreateData): ReplyCallbackAction = slash.reply(message)

    fun reply(content: String): ReplyCallbackAction = slash.reply(content)

    fun replyEmbeds(embeds: MutableCollection<MessageEmbed>) = slash.replyEmbeds(embeds)

    fun replyEmbeds(embed: MessageEmbed, vararg embeds: MessageEmbed): ReplyCallbackAction =
        slash.replyEmbeds(embed, *embeds)

    fun replyComponents(component: MutableCollection<LayoutComponent>): ReplyCallbackAction =
        slash.replyComponents(component)

    fun replyComponents(component: LayoutComponent, vararg components: LayoutComponent): ReplyCallbackAction =
        slash.replyComponents(component, *components)

    @Deprecated(
        message = "Use default kotlin formatting.",
        replaceWith = ReplaceWith("slashInteraction.replyFormat(format, args)"),
        DeprecationLevel.ERROR
    )
    @ApiStatus.ScheduledForRemoval(inVersion = "1.2")
    fun replyFormat(format: String, vararg args: Any): ReplyCallbackAction = slash.replyFormat(format, args)

    fun replyFiles(files: MutableCollection<FileUpload>) = slash.replyFiles(files)

    fun replyFiles(vararg files: FileUpload) = slash.replyFiles(*files)

    fun getOptionsByName(name: String): List<OptionMapping> = slash.getOptionsByName(name)

    fun getOptionsByType(type: OptionType): List<OptionMapping> = slash.getOptionsByType(type)

    fun getOption(name: String): OptionMapping? = slash.getOption(name)

    fun <T> getOption(name: String, resolver: (OptionMapping) -> T): T? = slash.getOption(name, resolver)

    fun <T> getOption(name: String, fallback: T, resolver: (OptionMapping) -> T): T? =
        slash.getOption(name, fallback, resolver)

    fun <T> getOption(name: String, fallback: Supplier<out T>, resolver: Function<in OptionMapping, out T>): T? =
        slash.getOption(name, fallback, resolver)
}

data class CommandModalInteraction(val interaction: ModalInteraction)

data class OptionData(
    val optionType: OptionType,
    val name: String,
    val description: String = "Description not provided for this option.",
    val required: Boolean = false,
    val autoComplete: Boolean = false
)