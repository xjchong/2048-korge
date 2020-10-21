package constants

import com.soywiz.klock.seconds
import com.soywiz.korge.html.Html
import com.soywiz.korim.font.BitmapFont
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.properties.Delegates

object GameConfig {

    const val BOARD_WIDTH_IN_CELLS = 4

    const val SWIPE_THRESHOLD = 20.0

    const val STORAGE_HISCORE_KEY = "hiscore"
    const val STORAGE_SOUND_KEY = "sound"

    val MOVE_DURATION = 0.1.seconds
    val MERGE_DURATION = 0.04.seconds

    val RESTART_IMAGE_FILE = resourcesVfs["restart.png"]
    val SOUND_ON_IMAGE = resourcesVfs["sound_on.png"]
    val SOUND_OFF_IMAGE = resourcesVfs["sound_off.png"]

    var FONT: BitmapFont by Delegates.notNull()
        private set

    suspend fun init() {
        FONT = resourcesVfs["clear_sans.fnt"].readBitmapFont()
    }
}