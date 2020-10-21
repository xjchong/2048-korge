package scenes.game

import com.soywiz.korev.Key
import com.soywiz.korge.animate.Animator
import com.soywiz.korge.animate.animateSequence
import com.soywiz.korge.html.Html
import com.soywiz.korge.input.SwipeDirection
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onKeyDown
import com.soywiz.korge.input.onSwipe
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tween.*
import com.soywiz.korge.ui.TextFormat
import com.soywiz.korge.ui.TextSkin
import com.soywiz.korge.ui.uiText
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.ObservableProperty
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korma.geom.vector.roundRect
import com.soywiz.korma.interpolation.Easing
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

    private var board = Board()
    private var isAnimating: Boolean = false

    private val score = ObservableProperty(0)
    private val hiscore = ObservableProperty(0)

    private val blocks = mutableMapOf<BoardPosition, BlockView?>()
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
                    Dimensions.CELL_WIDTH + (Dimensions.CELL_SPACING * 2),
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
        score.observe { value ->
            if (value > hiscore.value) hiscore.update(value)
        }

        hiscore.observe {
            // TODO: Update in storage.
        }

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

                text(hiscore.value.toString(), Dimensions.LARGE_FONT, Colors.WHITE, GameConfig.FONT) {
                    centerXOn(it)
                    alignBottomToBottomOf(it, Dimensions.SCORE_PADDING)

                    hiscore.observe { value ->
                        text = value.toString()
                    }
                }
            }

            scoreRect = roundRect(
                    scoreWidth,
                    Dimensions.SCORE_HEIGHT,
                    Dimensions.CORNER_RADIUS,
                    color = GameColors.UI_BACKGROUND
            ) {
                centerXBetween(titleRect.x + titleRect.width, hiscoreRect.x)
                alignTopToTopOf(titleRect)
            }.also {
                text(Strings.SCORE_TITLE, Dimensions.SMALL_FONT, Colors.WHITE, GameConfig.FONT)
                        .centerXOn(it)
                        .alignTopToTopOf(it, Dimensions.SCORE_PADDING)
                text(score.value.toString(), Dimensions.LARGE_FONT, Colors.WHITE, GameConfig.FONT) {
                    centerXOn(it)
                    alignBottomToBottomOf(it, Dimensions.SCORE_PADDING)

                    score.observe { value ->
                        text = value.toString()
                    }
                }
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

                onClick { restart() }
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
            onKeyDown { event ->
                when (event.key) {
                    Key.UP -> Up
                    Key.DOWN -> Down
                    Key.LEFT -> Left
                    Key.RIGHT -> Right
                    else -> null
                }?.let { direction ->
                    moveBlocks(direction)
                }
            }

            onSwipe(GameConfig.SWIPE_THRESHOLD) {
                val moveDirection = when (it.direction) {
                    SwipeDirection.TOP ->  Up
                    SwipeDirection.BOTTOM -> Down
                    SwipeDirection.LEFT -> Left
                    SwipeDirection.RIGHT -> Right
                }

                moveBlocks(moveDirection)
            }
        }
    }

    private fun addRandomBlock(): Boolean {
        val randomPosition = board.numberMap.filter { it.value == null }.keys.shuffled().firstOrNull() ?: return false
        val randomNumber = if (Random.nextDouble() < 0.9) 2 else 4

        addBlock(randomPosition, randomNumber)

        return true
    }

    private fun addBlock(boardPosition: BoardPosition, number: Int) {
        blocks[boardPosition] = BlockView(number).addTo(root).position(boardPosition)
        board.numberMap[boardPosition] = number
    }

    private fun getScreenPosition(boardPosition: BoardPosition): Pair<Double, Double> {
        val nextX = with (Dimensions) {
            boardRect.x + CELL_SPACING + (CELL_WIDTH + CELL_SPACING) * boardPosition.x
        }

        val nextY = with (Dimensions) {
            boardRect.y + CELL_SPACING + (CELL_WIDTH + CELL_SPACING) * boardPosition.y
        }

        return Pair(nextX, nextY)
    }

    private fun BlockView.position(boardPosition: BoardPosition): BlockView {
        val nextPosition = getScreenPosition(boardPosition)

        return position(nextPosition.first, nextPosition.second)
    }

    private fun moveBlocks(direction: MoveDirection) {
        if (isAnimating) return

        if (!board.hasMovesRemaining) {
            root.showGameOver { restart() }

            return
        }

        val moveChanges = board.calculateMove(direction)
        val resultingBoard = moveChanges.resultingBoard

        // Add pending merged values to score (e.g., 2 + 2 = 4, add 4 to score).
        score.update(moveChanges.merges.sumBy { merge ->
            val blockView = blocks[merge.from1] ?: return@sumBy 0

            blockView.number * 2
        } + score.value)

        if (resultingBoard.numberMap == board.numberMap) {
            return
        } else {
            isAnimating = true
            root.animateMove(moveChanges) {
                board = resultingBoard
                addRandomBlock()
                board.print()
                isAnimating = false
            }
        }
    }

    private fun Container.showGameOver(onRestart: () -> Unit) = container {
        val textFormat = TextFormat(
                color = Colors.WHITE,
                size = Dimensions.LARGE_FONT.toInt(),
                font = Html.FontFace.Bitmap(GameConfig.FONT))

        val textSkin = TextSkin(
                normal = textFormat,
                over = textFormat.copy(color = GameColors.TEXT_HIGHLIGHT),
                down = textFormat.copy(color = GameColors.TEXT_SELECT))

        fun triggerRestart() {
            this@container.removeFromParent()
            onRestart()
        }

        position(boardRect.x, boardRect.y)
        roundRect(boardRect.width, boardRect.height, Dimensions.CORNER_RADIUS, color = GameColors.OVERLAY_BACKGROUND)

        text(Strings.GAME_OVER_TITLE, 60.0, Colors.WHITE, GameConfig.FONT) {
            centerBetween(0.0, 0.0, boardRect.width, boardRect.height)
            y -= 60
        }

        uiText(Strings.RESTART_CTA_TITLE, boardRect.width, skin = textSkin) {
            centerBetween(0.0, 0.0, boardRect.width, boardRect.height)
            y += 20
            onClick { triggerRestart() }
        }

        onKeyDown {
            if (it.key == Key.ENTER || it.key == Key.SPACE) {
                triggerRestart()
            }
        }
    }

    private fun restart() {
        board = Board()
        blocks.values.forEach { it?.removeFromParent() }
        blocks.clear()
        score.update(0)

        addRandomBlock()
    }

    private fun Container.animateMove(
            moveChanges: MoveChanges,
            onEnd: () -> Unit
    ) = launchImmediately {
        animateSequence {
            val oldBlocks = blocks.toMap()

            parallel {

                for (move in moveChanges.moves) {
                    val blockView = oldBlocks[move.from] ?: continue
                    val (nextX, nextY) = getScreenPosition(move.to)

                    blockView.moveTo(nextX, nextY, GameConfig.MOVE_DURATION, Easing.LINEAR)
                    blocks[move.from] = null
                    blocks[move.to] = blockView
                }

                for (merge in moveChanges.merges) {
                    val blockView1 = oldBlocks[merge.from1] ?: continue
                    val blockView2 = oldBlocks[merge.from2] ?: continue
                    val (nextX, nextY) = getScreenPosition(merge.to)

                    sequence {
                        parallel {
                            blockView1.moveTo(nextX, nextY, GameConfig.MOVE_DURATION, Easing.LINEAR)
                            blockView2.moveTo(nextX, nextY, GameConfig.MOVE_DURATION, Easing.LINEAR)
                        }

                        block {
                            val nextNumber = blockView1.number * 2

                            blockView1.removeFromParent()
                            blockView2.removeFromParent()
                            addBlock(merge.to, nextNumber)
                        }

                        sequenceLazy {
                            blocks[merge.to]?.let { animateMerge(it) }
                        }
                    }
                }
            }
            block {
                onEnd()
            }
        }
    }

    private fun Animator.animateMerge(blockView: BlockView) {
        val x = blockView.x
        val y = blockView.y
        val scale = blockView.scale

        tween(
                blockView::x[x - 4],
                blockView::y[y - 4],
                blockView::scale[scale + 0.1],
                time = GameConfig.MERGE_DURATION,
                easing = Easing.LINEAR
        )

        tween(
                blockView::x[x],
                blockView::y[y],
                blockView::scale[scale],
                time = GameConfig.MERGE_DURATION,
                easing = Easing.LINEAR
        )
    }
}
