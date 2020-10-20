package views

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import constants.Dimensions
import constants.GameColors
import constants.GameConfig

class BlockView(val number: Int) : Container() {

    companion object {

        private fun backgroundColorForNumber(number: Int): RGBA {
            with (GameColors) {
                return when (number) {
                    2 -> CELL_2
                    4 -> CELL_4
                    8 -> CELL_8
                    16 -> CELL_16
                    32 -> CELL_32
                    64 -> CELL_64
                    128 -> CELL_128
                    256 -> CELL_256
                    512 -> CELL_512
                    1024 -> CELL_1024
                    2048 -> CELL_2048
                    4096 -> CELL_4096
                    8192 -> CELL_8192
                    16384 -> CELL_16384
                    32768 -> CELL_32768
                    65536 -> CELL_65536
                    131072 -> CELL_131072
                    else -> Colors.BLACK
                }
            }
        }

        private fun textColorForNumber(number: Int): RGBA {
            return if (number <= 4) Colors.BLACK else Colors.WHITE
        }
    }

    init {
        val title = number.toString()
        val textSize = (Dimensions.CELL_WIDTH - (2 * Dimensions.CELL_PADDING)) / (1 + (0.1 * title.length))

        val blockRect = roundRect(
                Dimensions.CELL_WIDTH,
                Dimensions.CELL_WIDTH,
                Dimensions.CORNER_RADIUS,
                color = backgroundColorForNumber(number))

        text(number.toString(), textSize, textColorForNumber(number), GameConfig.FONT) {
            centerOn(blockRect)
        }
    }
}