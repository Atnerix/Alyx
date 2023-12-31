package com.atnerix.alyx.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class TrackAdapter(val player: AudioPlayer): AudioEventAdapter() {
    private val queue: BlockingQueue<AudioTrack> = LinkedBlockingQueue()

    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) queue.offer(track)
    }

    fun nextTrack() = player.startTrack(queue.poll(), false)

    fun pause() {
        player.isPaused = !player.isPaused
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        val nonNullReason = endReason ?: return

        if (nonNullReason.mayStartNext && queue.isNotEmpty()) nextTrack()
    }
}