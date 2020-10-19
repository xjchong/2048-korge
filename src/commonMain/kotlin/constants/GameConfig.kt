package constants

import com.soywiz.korio.file.std.resourcesVfs

object GameConfig {

    const val BOARD_WIDTH_IN_CELLS = 4

    val FONT_FILE = resourcesVfs["clear_sans.fnt"]
    val RESTART_IMAGE_FILE = resourcesVfs["restart.png"]
    val UNDO_IMAGE_FILE = resourcesVfs["undo.png"]
}