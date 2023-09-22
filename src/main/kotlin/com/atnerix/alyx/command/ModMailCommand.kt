package com.atnerix.alyx.command

import com.atnerix.alyx.api.CommandModalInteraction
import com.atnerix.alyx.api.Range
import com.atnerix.alyx.api.SlashInteraction
import com.atnerix.alyx.api.SlashedCommand
import com.atnerix.alyx.jda
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import java.awt.Color
import kotlin.random.Random
import kotlin.random.asJavaRandom

open class ModMailCommand: SlashedCommand {
    override fun invokeCommand(interaction: SlashInteraction) {
        val shortInput = this.createTextInput("name", "Title", range = Range(5, 100))
        val paragraphInput = this.createTextInput(
            "desc",
            "Description",
            TextInputStyle.PARAGRAPH,
            "Please, describe your problem here",
            Range(10, 4000)
        )

        interaction.slash.replyModal(this.createModal("mail_box", "Test command",  ActionRow.of(shortInput), ActionRow.of(paragraphInput))).queue()
    }

    override fun getModals(): Map<String, CommandModalInteraction.() -> Unit> = hashMapOf(
        Pair("mail_box") {
            val guild = this.interaction.guild ?: return@Pair
            val roles = guild.roles
            val title = this.getValue("name")
            val desc = this.getValue("desc")
            val random = Random.asJavaRandom()
            val color = Color(
                (random.nextFloat() / 2f + 0.5).toFloat(),
                (random.nextFloat() / 2f + 0.5).toFloat(),
                (random.nextFloat() / 2f + 0.5).toFloat()
            )
            val embedBuilder = EmbedBuilder()

            embedBuilder.setTitle(title?.asString)
                .setDescription(
                    """
                    ${desc?.asString}
                    """.trimIndent()
                )
                .setColor(color)
                .setFooter(this.interaction.member?.user?.globalName, this.interaction.member?.avatarUrl)

            if (roles.isNotEmpty()) {
                for (role in roles) {
                    val roleID = role.id
                    if (roleID == "1154786057712308266") {
                        jda.getUserById("471679889033003008")?.openPrivateChannel()?.flatMap { channel -> channel.sendMessageEmbeds(embedBuilder.build()) }?.queue()
                        jda.getUserById("603507025388765208")?.openPrivateChannel()?.flatMap { channel -> channel.sendMessageEmbeds(embedBuilder.build()) }?.queue()
                        this.reply("Message has been deviled!").setEphemeral(true).queue()
                    } else this.reply("Sorry! I can't found modmail role").setEphemeral(true).queue()
                }
            }
        }
    )

    override fun getName(): String = "modmail"
}