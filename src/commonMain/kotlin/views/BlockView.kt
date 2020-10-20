package views

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import constants.Dimensions
import constants.GameConfig

class BlockView(val number: Int) : Container() {

    companion object {

        private fun backgroundColorForNumber(number: Int): RGBA {
            return Colors.RED
        }

        private fun textColorForNumber(number: Int): RGBA {
            return Colors.WHITE
        }
    }

    init {
        val title = number.toString()
        val textSize = (Dimensions.CELL_WIDTH - (2 * Dimensions.CELL_PADDING)) / title.length

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