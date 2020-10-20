package constants

import com.soywiz.klock.seconds
import com.soywiz.korim.font.BitmapFont
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.properties.Delegates

object GameConfig {

    const val BOARD_WIDTH_IN_CELLS = 4

    const val SWIPE_THRESHOLD = 20.0

    val MOVE_DURATION = 0.1.seconds
    val MERGE_DURATION = 0.04.seconds

    val RESTART_IMAGE_FILE = resourcesVfs["restart.png"]
    val UNDO_IMAGE_FILE = resourcesVfs["undo.png"]

    var FONT: BitmapFont by Delegates.notNull()
        private set

    suspend fun init() {
        FONT = resourcesVfs["clear_sans.fnt"].readBitmapFont()
    }
}