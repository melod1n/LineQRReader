package com.meloda.lineqrreader.util

import android.content.Context
import android.media.MediaPlayer

object SoundUtils {

    fun playSuccessSound(context: Context) {
        val player = MediaPlayer()

        try {
            val descriptor = context.assets.openFd("sound_success.ogg")

            player.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )

            player.setVolume(0.1f, 0.1f)
            player.setOnPreparedListener(MediaPlayer::start)
            player.setOnCompletionListener(MediaPlayer::release)
            player.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}