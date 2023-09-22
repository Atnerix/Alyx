package com.atnerix.alyx.command

import com.atnerix.alyx.api.OptionData
import com.atnerix.alyx.api.SlashInteraction
import com.atnerix.alyx.api.SlashedCommand
import com.atnerix.alyx.logger
import com.atnerix.alyx.player.PlayAction
import com.atnerix.alyx.player.PlayerManager
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.net.URI
import java.net.URISyntaxException

class PlayCommand: SlashedCommand {
    override fun invokeCommand(interaction: SlashInteraction) {
        val option = interaction.getOption("music") ?: return
        val guild = interaction.guild ?: return
        val member = interaction.member ?: return
        val self = interaction.self ?: return
        val memberVoice = member.voiceState ?: return
        val selfVoice = self.voiceState ?: return

        if (!memberVoice.inAudioChannel()) {
            logger.debug("User '${member.effectiveName}' not in a voice channel")
            interaction.reply("You are not in an voice channel").setEphemeral(false).queue()
        }

        val audioManager = guild.audioManager
        val voiceChannel = memberVoice.channel

        if (!selfVoice.inAudioChannel()) {
            logger.debug("Open Audio connection for '{}' channel..", voiceChannel)
            audioManager.openAudioConnection(voiceChannel)
        }

        if (memberVoice != selfVoice.channel && selfVoice.inAudioChannel()) {
            interaction.reply("You must be in the same channel as the bot").setEphemeral(true).queue()
            return
        }

        var link = option.asString

        if (!this.isUrl(link)) link = "ytsearch:$link"

        val action = PlayAction(interaction, link)

        PlayerManager.getInstance().loadAndPlay(action)
    }

    override fun getOptions(): ArrayList<OptionData> =
        arrayListOf(
            OptionData(OptionType.STRING, "music", "Url or name of song", true)
        )

    override fun getName(): String = "play"

    override fun getDescription(): String = "Plays a song"

    private fun isUrl(url: String): Boolean = try {
        URI(url)
        logger.debug("Value '$url' - url")
        true
    } catch (e: URISyntaxException) {
        logger.debug("Value '$url' is not url")
        false
    }
}