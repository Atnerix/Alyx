package com.atnerix.alyx.utils

import com.atnerix.alyx.api.CommandModalInteraction
import com.atnerix.alyx.api.SlashInteraction
import com.atnerix.alyx.api.SlashedCommand
import com.atnerix.alyx.command.PlayCommand
import com.atnerix.alyx.jda
import com.atnerix.alyx.logger
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.reflect.KClass

class AlyxListener: ListenerAdapter() {
    init {
        command(PlayCommand::class)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val interaction = SlashInteraction(event.interaction, event.channel)

        for (command in listOfCommands) {
            when(event.name) {
                command.getName() -> {
                    command.invokeCommand(interaction)
                    logger.debug("Command '${command.getName()}' invoked")
                }
            }
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        val modal = CommandModalInteraction(event)
        registerAllModals(modal)
    }

    private fun <R: SlashedCommand> commandNotClass(command: R) {
        listOfCommands.add(command)
    }

    private fun <R: SlashedCommand> command(clazz: KClass<R>) {
        listOfCommands.add(clazz.java.getDeclaredConstructor().newInstance())
    }

    private fun <R: SlashedCommand> commandWithInstance(clazz: KClass<R>, vararg instances: Any) {
        listOfCommands.add(clazz.java.getDeclaredConstructor().newInstance(instances))
    }

    companion object {
        private val listOfCommands: MutableList<SlashedCommand> = mutableListOf()

        @JvmStatic
        fun registerAllCommands() {
            if (listOfCommands.isNotEmpty()) {
                for (commands in listOfCommands) {
                    val desc = commands.getDescription().ifEmpty { "This command doesn't have a description! Please, say it to developer!" }
                    val command = jda.upsertCommand(commands.getName().lowercase(), desc)

                    if (commands.getOptions().isNotEmpty()) {
                        for (option in commands.getOptions()) {
                            command.addOption(option.optionType, option.name, option.description, option.required, option.autoComplete)
                        }
                    }

                    command.queue()

                    logger.debug("Command '${commands.getName()}' are registered!")
                }
            }
        }

        @JvmStatic
        private fun registerAllModals(modalInteraction: CommandModalInteraction) {
            if (listOfCommands.isNotEmpty())
                for (command in listOfCommands)
                    if (command.hasModal())
                        if (modalInteraction.interaction.modalId == command.getName().lowercase())
                            command.invokeModal(modalInteraction)
        }
    }

}
