package models

import constants.GameConfig
import views.BlockView

class Board {

    val blocks: MutableMap<BoardPosition, BlockView?> = {
        val map = mutableMapOf<BoardPosition, BlockView?>()

        for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
            for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
               map[BoardPosition(column, row)] = null
            }
        }

        map
    }()
}