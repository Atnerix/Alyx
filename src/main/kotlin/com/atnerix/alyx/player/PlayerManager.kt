package com.atnerix.alyx.player

import com.atnerix.alyx.logger
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild

class PlayerManager {
    private val managers: MutableMap<Long, GuildMusicManager> = hashMapOf()
    private val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)
    }

    fun getMusicManager(guild: Guild): GuildMusicManager {
        return managers.computeIfAbsent(guild.idLong) {
            val guildMusicManager = GuildMusicManager(playerManager)
            guild.audioManager.sendingHandler = guildMusicManager.sendHandler
            guildMusicManager
        }
    }

    fun loadAndPlay(action: PlayAction) {
        val slash = action.interaction
        val trackUri = action.trackUrl
        val musicManager = slash.guild?.let { this.getMusicManager(it) } ?: return
        playerManager.loadItemOrdered(musicManager, trackUri, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack?) {
                if (track == null) return
                musicManager.trackAdapter.queue(track)
                slash.reply("Add to playlist `${track.info.title}` by `${track.info.author}`.").queue()
                logger.debug("Loading track with name: ${track.info.title}, author: ${track.info.author}")
            }

            override fun playlistLoaded(playlist: AudioPlaylist?) {
                if (playlist == null) return

                val tracks = playlist.tracks
                if (trackUri.contains("ytsearch")) {
                    val track = tracks[0]
                    musicManager.trackAdapter.queue(track)
                    slash.reply("Add to playlist `${track.info.title}` by `${track.info.author}`.").queue()
                    logger.debug("Loading single track with name: ${track.info.title}, author: ${track.info.author}")
                } else {
                    slash.reply("Add `${tracks.size}` from `${playlist.name}`").queue()
                    for (track in tracks) {
                        musicManager.trackAdapter.queue(track)
                    }
                    logger.debug("Added `${playlist.name}` playlist to bot playlist with `${tracks.size}` tracks")
                }
            }

            override fun noMatches() {
                logger.warn("Track(s) with name $trackUri not found!")
            }

            override fun loadFailed(exception: FriendlyException?) {
                logger.error("Enable to load ${trackUri}.", exception)
            }

        })
    }

    fun pauseOrUnpause(action: PlayAction) {
        val musicManager = action.interaction.guild?.let { this.getMusicManager(it) } ?: return

        musicManager.trackAdapter.pause()
    }

    fun isPaused(action: PlayAction): Boolean {
        val musicManager = action.interaction.guild!!.let { this.getMusicManager(it) }
        return musicManager.trackAdapter.player.isPaused
    }

    companion object {
        private var instance: PlayerManager? = null

        @JvmStatic
        fun getInstance(): PlayerManager {
            if (instance == null) instance = PlayerManager()
            return instance!!
        }
    }
}