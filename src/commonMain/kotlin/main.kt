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
    val titleWidth = Dimensions.CELL_WIDTH
    val scoreWidth = (Dimensions.SCREEN_WIDTH - (4 * Dimensions.BOARD_MARGIN) - titleWidth) / 2

    val boardRect = roundRect(
            Dimensions.BOARD_WIDTH,
            Dimensions.BOARD_WIDTH,
            Dimensions.CORNER_RADIUS,
            color = GameColors.BOARD_BACKGROUND) {
        centerXOnStage()
        alignBottomToBottomOf(this@Korge, Dimensions.BOARD_MARGIN)
    }

    graphics {
        alignLeftToLeftOf(boardRect)
        alignTopToTopOf(boardRect)

        fill(GameColors.EMPTY_CELL_BACKGROUND) {
            for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                    with (Dimensions) {
                        val x = CELL_SPACING + (CELL_SPACING + CELL_WIDTH) * row
                        val y = CELL_SPACING + (CELL_SPACING + CELL_WIDTH) * column

                        roundRect(x, y, CELL_WIDTH, CELL_WIDTH, CORNER_RADIUS)
                    }
                }
            }
        }
    }


    val titleRect = roundRect(
            Dimensions.CELL_WIDTH,
            Dimensions.CELL_WIDTH,
            Dimensions.CORNER_RADIUS,
            color = GameColors.TITLE_BACKGROUND
    ) {
        alignLeftToLeftOf(boardRect)
        alignTopToTopOf(this@Korge, Dimensions.TITLE_MARGIN)
    }.also {
        text(Strings.TITLE, Dimensions.LARGE_FONT, Colors.WHITE, font).centerOn(it)
    }

    val hiscoreRect = roundRect(
            scoreWidth,
            Dimensions.SCORE_HEIGHT,
            Dimensions.CORNER_RADIUS,
            color = GameColors.UI_BACKGROUND
    ) {
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
            Dimensions.SCORE_HEIGHT,
            Dimensions.CORNER_RADIUS,
            color = GameColors.UI_BACKGROUND
    ) {
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

    val buttonWidth = Dimensions.BUTTON_WIDTH
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
