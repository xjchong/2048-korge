package models

data class MoveChanges(val resultingBoard: Board, val moves: List<MoveChange>, val merges: List<MergeChange>)

data class MoveChange(val from: BoardPosition, val to: BoardPosition)
data class MergeChange(val from1: BoardPosition, val from2: BoardPosition, val to: BoardPosition)