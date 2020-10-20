package scenes.game

import com.soywiz.kds.IntArray2
import constants.GameConfig
import models.BoardPosition
import views.BlockView

class Board(private val array: IntArray2 = IntArray2(
        GameConfig.BOARD_WIDTH_IN_CELLS,
        GameConfig.BOARD_WIDTH_IN_CELLS,
        -1)
) {

    val blocks: MutableMap<BoardPosition, BlockView> = mutableMapOf()
}