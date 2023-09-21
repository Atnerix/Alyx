package com.atnerix.alyx.utils

import com.atnerix.alyx.api.SlashInteraction
import com.atnerix.alyx.api.SlashedCommand
import com.atnerix.alyx.api.TestCommand
import com.atnerix.alyx.jda
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.reflect.KClass

class AlyxListener: ListenerAdapter() {
    init {
        command(TestCommand::class)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val interaction = SlashInteraction(event.interaction, event.channel)

        for (clazz in listOfCommands) {
            when(event.name) {
                clazz.getName() -> clazz.invokeCommand(interaction)
            }
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        super.onModalInteraction(event)
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
                    val command = jda.upsertCommand(commands.getName(), desc)

                    if (commands.getOptions().isNotEmpty()) {
                        for (option in commands.getOptions()) {
                            command.addOption(option.optionType, option.name, option.description, option.required, option.autoComplete)
                        }
                    }

                    command.queue()
                }
            }
        }

        @JvmStatic
        private fun registerAllModals() {
            if (listOfCommands.isNotEmpty()) {
                for (command in listOfCommands) {

                }
            }
        }
    }
}