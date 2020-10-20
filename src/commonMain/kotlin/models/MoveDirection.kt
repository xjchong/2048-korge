package models

sealed class MoveDirection
object Up : MoveDirection()
object Down : MoveDirection()
object Left : MoveDirection()
object Right : MoveDirection()
