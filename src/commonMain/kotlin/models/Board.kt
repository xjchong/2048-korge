package models

import constants.GameConfig

class Board {

    val numberMap: MutableMap<BoardPosition, Int?> = {
        val map = mutableMapOf<BoardPosition, Int?>()

        for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
            for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
               map[BoardPosition(column, row)] = null
            }
        }

        map
    }()

    val hasMovesRemaining: Boolean
        get() = {
            numberMap.any { entry ->
                entry.key.adjacentPositions.any { position ->
                    numberMap[position]?.equals(entry.value) ?: true
                }
            }
        }()

    private fun copy(): Board {
        return Board().also { it.numberMap.putAll(numberMap) }
    }

    fun calculateMove(moveDirection: MoveDirection): MoveChanges {
        val moves = mutableListOf<MoveChange>()
        val merges = mutableListOf<MergeChange>()
        val boardCopy = Board()

        // For a row like [ ] [1] [2] [2], and move direction RIGHT, a line represents: [ ] [1] [2] [2].
        for (line in getLinesForDirection(moveDirection)) {

            // Without blanks, the example line is [1] [2] [2]. We will use this as a queue.
            var queue = line.filter { it.second != null }.reversed()

            // The next line is empty at first, and we will fill it in with the new values.
            val nextLine = mutableListOf<Pair<BoardPosition, Int?>>()

            for (boardPosition in line.map { it.first }) {
                // We must assign something to nextLine here.
                // First check our 'queue', which at first is [1] [2] [2]
                when (queue.count()) {
                    0 -> {
                        // If there's nothing left in the queue, assign 'empty' blocks.
                        nextLine.add(Pair(boardPosition, null))
                    }
                    1 -> {
                        // If there's only one thing remaining, it immediately goes into the new line.
                        nextLine.add(Pair(boardPosition, queue.last().second))
                        moves.add(MoveChange(queue.last().first, boardPosition))
                        queue = queue.dropLast(1)
                    }
                    else -> {
                        // There's at least two things left in the queue. Check if we can merge the last two.
                        val lastTwoThings = queue.takeLast(2)

                        queue = if (lastTwoThings[0].second == lastTwoThings[1].second) {
                            val from1 = queue.last()
                            val from2 = queue.dropLast(1).last()
                            val nextNumber = (from1.second ?: 0) * 2

                            nextLine.add(Pair(boardPosition, nextNumber))
                            merges.add(MergeChange(from1.first, from2.first, boardPosition))
                            queue.dropLast(2)
                        } else {
                            nextLine.add(Pair(boardPosition, queue.last().second))
                            moves.add(MoveChange(queue.last().first, boardPosition))
                            queue.dropLast(1)
                        }
                    }
                }
            }

            for (block in nextLine) {
                boardCopy.numberMap[block.first] = block.second
            }
        }

        println(moves)
        println(merges)

        return MoveChanges(boardCopy, moves, merges)
    }

    private fun getLinesForDirection(moveDirection: MoveDirection): List<MutableList<Pair<BoardPosition, Int?>>> {
        val lines = mutableListOf<MutableList<Pair<BoardPosition, Int?>>>()

        when (moveDirection) {
            Down -> {
                for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                    val line = mutableListOf<Pair<BoardPosition, Int?>>()

                    for (row in (GameConfig.BOARD_WIDTH_IN_CELLS - 1) downTo 0) {
                        val position = BoardPosition(column, row)

                        line.add(Pair(position, numberMap[position]))
                    }

                    lines.add(line)
                }
            }
            Up -> {
                for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                    val line = mutableListOf<Pair<BoardPosition, Int?>>()

                    for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                        val position = BoardPosition(column, row)

                        line.add(Pair(position, numberMap[position]))
                    }

                    lines.add(line)
                }
            }
            Right -> {
                for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                    val line = mutableListOf<Pair<BoardPosition, Int?>>()

                    for (column in (GameConfig.BOARD_WIDTH_IN_CELLS - 1) downTo 0) {
                        val position = BoardPosition(column, row)

                        line.add(Pair(position, numberMap[position]))
                    }

                    lines.add(line)
                }
            }
            Left -> {
                for (row in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                    val line = mutableListOf<Pair<BoardPosition, Int?>>()

                    for (column in 0 until GameConfig.BOARD_WIDTH_IN_CELLS) {
                        val position = BoardPosition(column, row)

                        line.add(Pair(position, numberMap[position]))
                    }

                    lines.add(line)
                }
            }
        }

        return lines
    }

    fun print() {
        for (row in 0..3) {
            for (col in 0..3) {
                print(numberMap[BoardPosition(col, row)].toString().padEnd(5))
            }
            println()
        }
        println()
    }
}