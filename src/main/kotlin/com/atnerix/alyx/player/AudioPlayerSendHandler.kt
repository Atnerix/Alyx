package com.atnerix.alyx.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer

class AudioPlayerSendHandler(private val player: AudioPlayer) : AudioSendHandler {
    private val buf: ByteBuffer = ByteBuffer.allocate(2048)
    private val frame: MutableAudioFrame = MutableAudioFrame()

    init {
        this.frame.setBuffer(buf)
    }

    override fun canProvide(): Boolean = this.player.provide(this.frame)

    override fun provide20MsAudio(): ByteBuffer? = this.buf.flip()

    override fun isOpus(): Boolean = true
}