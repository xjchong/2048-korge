import com.soywiz.korge.*
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korim.color.RGBA
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import constants.Dimensions
import constants.GameColors
import constants.Strings
import scenes.game.GameScene
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
