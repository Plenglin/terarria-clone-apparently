package io.github.plenglin.platform

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import io.github.plenglin.platform.physics.landed.World
import io.github.plenglin.platform.physics.landed.entity.Player

/**
 * Primary screen for displaying the game.
 */
class GameScreen : Screen {
    //lateinit var batch: SpriteBatch
    lateinit var cam: OrthographicCamera
    //lateinit var sprite: Sprite
    //lateinit var debugRenderer: Box2DDebugRenderer
    lateinit var shape: ShapeRenderer
    //lateinit var playerRect: PhysicsRect
    var player = Player()
    var world = World(64)

    val ip = PlayerInputProcessor(this)

    override fun show() {
        Gdx.input.inputProcessor = InputMultiplexer(ip)

        cam = OrthographicCamera()
        shape = ShapeRenderer()
        player.pos.set(100f, 256f)
        for (x in 95..4095) {
            for (y in 0..192) {
                world[x, y] = Block.dirt
            }
        }
        world.addEntity(player)
        cam.zoom = 1/10f
    }

    override fun render(delta: Float) {
        cam.position.set(Vector3(player.pos, 0f))
        cam.update()
        player.velocity.x = ip.moving * 1

        Gdx.gl20.glClearColor(0f, 0f ,0f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        shape.projectionMatrix = cam.combined

        shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.color = Color.WHITE
        for (i in (player.pos.x.toInt()-30)..(player.pos.x.toInt()+30)) {
            for (j in (player.pos.y.toInt()-30)..(player.pos.y.toInt()+30)) {
                val x = i.toFloat()
                val y = j.toFloat()
                if (world[i, j] != null) {
                    shape.rect(x, y, 1f, -1f)
                }
            }
        }

        shape.color = Color.BLUE
        shape.rect(player.pos.x, player.pos.y, 1f, 2f)
        shape.end()

        world.update(minOf(delta, 1/60f))
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun resize(width: Int, height: Int) {
        cam.setToOrtho(false, width.toFloat(), height.toFloat())
    }

    override fun dispose() {

    }

}

class PlayerInputProcessor(val game: GameScreen) : InputProcessor {

    var moving = 0f

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        if (amount > 0) {
            game.cam.zoom *= 0.9f
        } else if (amount < 0) {
            game.cam.zoom /= 0.9f
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.A, Input.Keys.D -> {
                moving = 0f
                true
            }
            else -> false
        }
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.A -> {
                moving = -1f
                true
            }
            Input.Keys.D -> {
                moving = 1f
                true
            }
            Input.Keys.SPACE -> {
                if (game.player.hasLanded) {
                    game.player.velocity.add(0f, 10f)
                }
                true
            }
            else -> false
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return when (button) {
            Input.Buttons.LEFT -> {  // Destroy block
                val unprojected = game.cam.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                println(unprojected)
                val i = unprojected.x.toInt()
                val j = unprojected.y.toInt() + 1
                game.world[i, j] = null
                true
            }
            Input.Buttons.RIGHT -> {  // Place block
                val unprojected = game.cam.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                val i = unprojected.x.toInt()
                val j = unprojected.y.toInt() + 1
                game.world[i, j] = Block.dirt
                true
            }
            else -> false
        }
    }

}