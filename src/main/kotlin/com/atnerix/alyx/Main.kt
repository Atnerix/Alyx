package com.atnerix.alyx

import com.atnerix.alyx.api.runtime.Settings
import com.atnerix.alyx.api.runtime.readRuntimeJson
import com.atnerix.alyx.utils.AlyxListener
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.EnumSet

lateinit var jda: JDA
val logger: Logger = LoggerFactory.getLogger("Alyx Logger")

fun main(args: Array<String>) {
    logger.info("Hello! Initializing..")

    val intents = EnumSet.of(
        GatewayIntent.GUILD_VOICE_STATES,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.DIRECT_MESSAGES,
        GatewayIntent.MESSAGE_CONTENT,
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
        GatewayIntent.SCHEDULED_EVENTS,
        GatewayIntent.GUILD_MEMBERS
    )

    val json = Json.readRuntimeJson(Settings.serializer(), "settings")
    val builder = JDABuilder.createDefault(json.token, intents)
        .addEventListeners(AlyxListener())
        .enableCache(
            CacheFlag.VOICE_STATE,
            CacheFlag.EMOJI,
            CacheFlag.STICKER,
            CacheFlag.SCHEDULED_EVENTS
        )

    when (json.activityType) {
        "play" -> builder.setActivity(json.activity?.let { Activity.playing(it) })
        "stream" -> builder.setActivity(json.activity?.let { Activity.streaming(it, null) })
        "listen" -> builder.setActivity(json.activity?.let { Activity.listening(it) })
        "watch" -> builder.setActivity(json.activity?.let { Activity.watching(it) })
        "complete" -> builder.setActivity(json.activity?.let { Activity.competing(it) })
        else -> logger.warn("Activity '${json.activityType}' with desc '${json.activity}' is not supported!")
    }

    jda = builder.build()
    AlyxListener.registerAllCommands()
}
