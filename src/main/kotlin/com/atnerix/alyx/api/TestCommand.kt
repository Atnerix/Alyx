package com.atnerix.alyx.api

import com.atnerix.alyx.logger

class TestCommand : SlashedCommand {
    override fun invokeCommand(interaction: SlashInteraction) {
        logger.info("Command works!")

        interaction.slashInteraction.reply("Okay, it's work!").setEphemeral(true).queue()
    }

    override fun getName(): String = "test"
}