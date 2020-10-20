package constants

object Dimensions {

    const val SCREEN_WIDTH = 480
    const val SCREEN_HEIGHT = 600

    const val CORNER_RADIUS = 5.0

    const val TITLE_WIDTH = 90.0
    const val TITLE_MARGIN = 24.0

    const val SCORE_HEIGHT = 72.0
    const val SCORE_MARGIN = 24.0
    const val SCORE_PADDING = 2.0

    const val BUTTON_WIDTH = 30.0
    const val BUTTON_MARGIN_TOP = 8.0
    const val BUTTON_SPACING = 8.0

    const val CELL_WIDTH = 90.0
    const val CELL_SPACING = 12.0
    const val CELL_PADDING = 8.0

    const val BOARD_MARGIN = 30.0
    const val BOARD_WIDTH = (CELL_WIDTH * GameConfig.BOARD_WIDTH_IN_CELLS) +
            (CELL_SPACING * (GameConfig.BOARD_WIDTH_IN_CELLS + 1))

    const val LARGE_FONT = 48.0
    const val SMALL_FONT = 28.0
}