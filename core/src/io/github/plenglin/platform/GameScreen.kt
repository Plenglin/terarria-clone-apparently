package io.github.plenglin.platform

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import io.github.plenglin.platform.physics.landed.World

/**
 *
 */
class GameScreen : Screen {
    lateinit var batch: SpriteBatch
    lateinit var cam: OrthographicCamera
    lateinit var sprite: Sprite
    //lateinit var debugRenderer: Box2DDebugRenderer
    lateinit var shape: ShapeRenderer
    //lateinit var playerRect: PhysicsRect
    var playerPos: Vector2 = Vector2(25f, 2f)
    var world = World(64)
    var landed: Boolean = true

    override fun show() {
        for (x in 0..4095) {
            for (y in 0..192) {
                world[x, y] = Block.dirt
            }
        }
    }

    override fun render(delta: Float) {
        Gdx.gl20.glClearColor(0f, 0f ,0f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        world.update(delta)
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