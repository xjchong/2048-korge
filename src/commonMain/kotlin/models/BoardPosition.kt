package models

import constants.GameConfig

data class BoardPosition(val x: Int, val y: Int) {

    val adjacentPositions: List<BoardPosition>
        get() = {
            listOf(
                    BoardPosition(x - 1, y),
                    BoardPosition(x + 1, y),
                    BoardPosition(x, y - 1),
                    BoardPosition(x, y + 1)
            ).filter {
                it.x >= 0 && it.x < GameConfig.BOARD_WIDTH_IN_CELLS && it.y >= 0 && it.y < GameConfig.BOARD_WIDTH_IN_CELLS
            }
        }()
}