package constants

import com.soywiz.korau.sound.NativeSound
import com.soywiz.korau.sound.readSound
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.properties.Delegates

object GameSound {

    var MOVE_SOUND: NativeSound by Delegates.notNull()
        private set

    var NO_MOVE_SOUND: NativeSound by Delegates.notNull()
        private set

    var RESTART_SOUND: NativeSound by Delegates.notNull()
        private set
    
    private const val DEFAULT_VOLUME = 0.4

    suspend fun init() {
        MOVE_SOUND = resourcesVfs["move.mp3"].readSound()
        NO_MOVE_SOUND = resourcesVfs["no_move.mp3"].readSound()
        RESTART_SOUND = resourcesVfs["restart.mp3"].readSound()
    }

    fun play(sound: NativeSound, volume: Double = DEFAULT_VOLUME) {
        sound.play().volume = volume
    }
}