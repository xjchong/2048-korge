package views

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import constants.Dimensions
import constants.GameConfig

class Block(number: Int, backgroundColor: RGBA, textColor: RGBA = Colors.WHITE) : Container() {

    init {
        val title = number.toString()
        val textSize = (Dimensions.CELL_WIDTH - (2 * Dimensions.CELL_PADDING)) / title.length
        val blockRect = roundRect(Dimensions.CELL_WIDTH, Dimensions.CELL_WIDTH, Dimensions.CORNER_RADIUS, color = backgroundColor)

        text(number.toString(), textSize, textColor, GameConfig.FONT) {
            centerOn(blockRect)
        }
    }
}