package com.atnerix.alyx.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildMusicManager(manager: AudioPlayerManager) {
    val player: AudioPlayer
    val trackAdapter: TrackAdapter
    val sendHandler: AudioPlayerSendHandler

    init {
        player = manager.createPlayer()
        trackAdapter = TrackAdapter(player)
        player.addListener(trackAdapter)
        sendHandler = AudioPlayerSendHandler(player)
    }
}