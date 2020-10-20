import com.soywiz.korge.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import com.soywiz.korma.geom.vector.roundRect
import constants.Dimensions
import constants.GameColors
import constants.GameConfig
import constants.Strings
import scenes.GameScene
import views.Block
import kotlin.reflect.KClass


suspend fun main() = Korge(Korge.Config(module = object : Module() {

    override val mainScene: KClass<out Scene> = GameScene::class
    override val title: String = Strings.TITLE
    override val windowSize: SizeInt = SizeInt(Dimensions.SCREEN_WIDTH, Dimensions.SCREEN_HEIGHT)
    override val size: SizeInt = SizeInt(Dimensions.SCREEN_WIDTH, Dimensions.SCREEN_HEIGHT)
    override val bgcolor: RGBA = GameColors.SCREEN_BACKGROUND

    override suspend fun AsyncInjector.configure() {
        mapPrototype { GameScene() }
    }
}))
