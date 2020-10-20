package scenes.game

import com.soywiz.korev.Key
import com.soywiz.korge.input.onKeyDown
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korma.geom.vector.roundRect
import constants.Dimensions
import constants.GameColors
import constants.GameConfig
import constants.Strings
import models.*
import views.BlockView
import kotlin.random.Random

class GameScene : Scene() {

    private lateinit var titleRect: RoundRect
    private lateinit var hiscoreRect: RoundRect
    private lateinit var scoreRect: RoundRect
    private lateinit var undoButton: Container
    private lateinit var restartButton: Container
    private lateinit var boardRect: RoundRect

    private val board = Board()
    private val scoreWidth = (Dimensions.SCREEN_WIDTH - (4 * Dimensions.BOARD_MARGIN) - Dimensions.TITLE_WIDTH) / 2

    override suspend fun Container.sceneInit() {
        GameConfig.init()

        setupUI()
        setupInput()

        addRandomBlock()
    }

    private suspend fun setupUI() {
        setupBoardUI()
        setupTitleUI()
        setupScoreUI()
        setupButtonUI()
    }

    private fun setupBoardUI() {
        with (root) {
            boardRect = roundRect(
                    Dimensions.BOARD_WIDTH,
                    Dimensions.BOARD_WIDTH,
                    Dimensions.CORNER_RADIUS,
                    color = GameColors.BOARD_BACKGROUND) {
                centerXOnStage()
                alignBottomToBottomOf(root, Dimensions.BOARD_MARGIN)
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
        }
    }

    private fun setupTitleUI() {
        with (root) {
            titleRect = roundRect(
                    Dimensions.CELL_WIDTH,
                    Dimensions.CELL_WIDTH,
                    Dimensions.CORNER_RADIUS,
                    color = GameColors.TITLE_BACKGROUND
            ) {
                alignLeftToLeftOf(boardRect)
                alignTopToTopOf(root, Dimensions.TITLE_MARGIN)
            }.also {
                text(Strings.TITLE, Dimensions.LARGE_FONT, Colors.WHITE, GameConfig.FONT).centerOn(it)
            }
        }
    }

    private fun setupScoreUI() {
        with (root) {
            hiscoreRect = roundRect(
                    scoreWidth,
                    Dimensions.SCORE_HEIGHT,
                    Dimensions.CORNER_RADIUS,
                    color = GameColors.UI_BACKGROUND
            ) {
                alignRightToRightOf(boardRect)
                alignTopToTopOf(titleRect)
            }.also {
                text(Strings.HISCORE_TITLE, Dimensions.SMALL_FONT, Colors.WHITE, GameConfig.FONT)
                        .centerXOn(it)
                        .alignTopToTopOf(it, Dimensions.SCORE_PADDING)
                text("0", Dimensions.LARGE_FONT, Colors.WHITE, GameConfig.FONT)
                        .centerXOn(it)
                        .alignBottomToBottomOf(it, Dimensions.SCORE_PADDING)
            }

            scoreRect = roundRect(
                    scoreWidth,
                    Dimensions.SCORE_HEIGHT,
                    Dimensions.CORNER_RADIUS,
                    color = GameColors.UI_BACKGROUND
            ) {
                alignRightToLeftOf(hiscoreRect, Dimensions.SCORE_MARGIN)
                alignTopToTopOf(titleRect)
            }.also {
                text(Strings.SCORE_TITLE, Dimensions.SMALL_FONT, Colors.WHITE, GameConfig.FONT)
                        .centerXOn(it)
                        .alignTopToTopOf(it, Dimensions.SCORE_PADDING)
                text("0", Dimensions.LARGE_FONT, Colors.WHITE, GameConfig.FONT)
                        .centerXOn(it)
                        .alignBottomToBottomOf(it, Dimensions.SCORE_PADDING)
            }
        }
    }

    private suspend fun setupButtonUI() {
        val buttonImageWidth = Dimensions.BUTTON_WIDTH * 0.8

        with (root) {
            restartButton = container {
                val buttonRect = roundRect(
                        Dimensions.BUTTON_WIDTH,
                        Dimensions.BUTTON_WIDTH,
                        Dimensions.CORNER_RADIUS,
                        color = GameColors.UI_BACKGROUND)

                image(GameConfig.RESTART_IMAGE_FILE.readBitmap()) {
                    size(buttonImageWidth, buttonImageWidth)
                    centerOn(buttonRect)
                }

                alignTopToBottomOf(hiscoreRect, Dimensions.BUTTON_MARGIN_TOP)
                alignRightToRightOf(hiscoreRect)
            }

            undoButton = container {
                val buttonRect = roundRect(
                        Dimensions.BUTTON_WIDTH,
                        Dimensions.BUTTON_WIDTH,
                        Dimensions.CORNER_RADIUS,
                        color = GameColors.UI_BACKGROUND)

                image(GameConfig.UNDO_IMAGE_FILE.readBitmap()) {
                    size(buttonImageWidth, buttonImageWidth)
                    centerOn(buttonRect)
                }

                alignTopToTopOf(restartButton)
                alignRightToLeftOf(restartButton, Dimensions.BUTTON_SPACING)
            }
        }
    }

    private fun setupInput() {
        with (root) {
            onKeyDown {
                val moveDirection = when (it.key) {
                    Key.UP -> Up
                    Key.DOWN -> Down
                    Key.LEFT -> Left
                    else -> Right
                }

                moveBlocks(moveDirection)
            }
        }
    }

    private fun addRandomBlock(): Boolean {
        val randomPosition = board.blocks.filter { it.value == null }.keys.shuffled().firstOrNull() ?: return false
        val randomNumber = if (Random.nextDouble() < 0.9) 2 else 4

        board.blocks[randomPosition] = BlockView(randomNumber).addTo(root).position(randomPosition)

        return true
    }

    private fun BlockView.position(boardPosition: BoardPosition): BlockView {
        val nextX = with (Dimensions) {
            boardRect.x + CELL_SPACING + (CELL_WIDTH + CELL_SPACING) * boardPosition.x
        }

        val nextY = with (Dimensions) {
            boardRect.y + CELL_SPACING + (CELL_WIDTH + CELL_SPACING) * boardPosition.y
        }

        return position(nextX, nextY)
    }

    private fun moveBlocks(direction: MoveDirection) {
        println(direction)
    }
}
