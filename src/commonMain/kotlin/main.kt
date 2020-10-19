import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korma.geom.vector.roundRect
import constants.Dimensions
import constants.GameColors
import constants.GameConfig
import constants.Strings

suspend fun main() = Korge(
        width = Dimensions.SCREEN_WIDTH,
        height = Dimensions.SCREEN_HEIGHT,
        bgcolor = GameColors.SCREEN_BACKGROUND
) {
    val font = GameConfig.FONT_FILE.readBitmapFont()
    val boardWidth = (views.virtualWidth - (Dimensions.BOARD_MARGIN * 2))
    val boardMarginTop = Dimensions.SCREEN_HEIGHT - boardWidth - Dimensions.BOARD_MARGIN
    val totalSpacing = (GameConfig.BOARD_WIDTH_IN_CELLS + 1) * Dimensions.CELL_SPACING
    val cellWidth = (boardWidth - totalSpacing) / GameConfig.BOARD_WIDTH_IN_CELLS.toDouble()
    val titleWidth = cellWidth
    val scoreWidth = (Dimensions.SCREEN_WIDTH - (4 * Dimensions.BOARD_MARGIN) - titleWidth) / 2
    val scoreHeight = cellWidth * 0.8

    val boardRect = roundRect(
            boardWidth,
            boardWidth,
            Dimensions.CORNER_RADIUS,
            color = GameColors.BOARD_BACKGROUND) {
        position(Dimensions.BOARD_MARGIN, boardMarginTop)
    }

    graphics {
        position(Dimensions.BOARD_MARGIN, boardMarginTop)

        fill(GameColors.EMPTY_CELL_BACKGROUND) {
            for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                    with (Dimensions) {
                        val x = CELL_SPACING + (CELL_SPACING + cellWidth) * row
                        val y = CELL_SPACING + (CELL_SPACING + cellWidth) * column

                        roundRect(x, y, cellWidth, cellWidth, CORNER_RADIUS)
                    }
                }
            }
        }
    }


    val titleRect = roundRect(cellWidth, cellWidth, Dimensions.CORNER_RADIUS, color = GameColors.TITLE_BACKGROUND) {
        position(Dimensions.TITLE_MARGIN, Dimensions.TITLE_MARGIN)
    }.also {
        text(Strings.TITLE, Dimensions.LARGE_FONT, Colors.WHITE, font).centerOn(it)
    }

    val hiscoreRect = roundRect(
            scoreWidth,
            scoreHeight,
            Dimensions.CORNER_RADIUS,
            color = GameColors.UI_BACKGROUND) {
        alignRightToRightOf(boardRect)
        alignTopToTopOf(titleRect)
    }.also {
        text(Strings.HISCORE_TITLE, Dimensions.SMALL_FONT, Colors.WHITE, font)
                .centerXOn(it)
                .alignTopToTopOf(it, Dimensions.SCORE_PADDING)
        text("0", Dimensions.LARGE_FONT, Colors.WHITE, font)
                .centerXOn(it)
                .alignBottomToBottomOf(it, Dimensions.SCORE_PADDING)
    }

    val scoreRect = roundRect(
            scoreWidth,
            scoreHeight,
            Dimensions.CORNER_RADIUS,
            color = GameColors.UI_BACKGROUND) {
        alignRightToLeftOf(hiscoreRect, Dimensions.SCORE_MARGIN)
        alignTopToTopOf(titleRect)
    }.also {
        text(Strings.SCORE_TITLE, Dimensions.SMALL_FONT, Colors.WHITE, font)
                .centerXOn(it)
                .alignTopToTopOf(it, Dimensions.SCORE_PADDING)
        text("0", Dimensions.LARGE_FONT, Colors.WHITE, font)
                .centerXOn(it)
                .alignBottomToBottomOf(it, Dimensions.SCORE_PADDING)
    }

    val buttonWidth = cellWidth * 0.3
    val buttonImageWidth = buttonWidth * 0.8

    val restartButton = container {
        val buttonRect = roundRect(buttonWidth, buttonWidth, Dimensions.CORNER_RADIUS, color = GameColors.UI_BACKGROUND)

        image(GameConfig.RESTART_IMAGE_FILE.readBitmap()) {
            size(buttonImageWidth, buttonImageWidth)
            centerOn(buttonRect)
        }

        alignTopToBottomOf(hiscoreRect, Dimensions.BUTTON_MARGIN_TOP)
        alignRightToRightOf(hiscoreRect)
    }

    val undoButton = container {
        val buttonRect = roundRect(buttonWidth, buttonWidth, Dimensions.CORNER_RADIUS, color = GameColors.UI_BACKGROUND)

        image(GameConfig.UNDO_IMAGE_FILE.readBitmap()) {
            size(buttonImageWidth, buttonImageWidth)
            centerOn(buttonRect)
        }

        alignTopToTopOf(restartButton)
        alignRightToLeftOf(restartButton, Dimensions.BUTTON_SPACING)
    }
}
