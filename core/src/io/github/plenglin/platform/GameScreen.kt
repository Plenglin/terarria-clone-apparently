package io.github.plenglin.platform

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import io.github.plenglin.platform.physics.landed.World
import io.github.plenglin.platform.physics.landed.entity.Player

/**
 *
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

    override fun show() {
        cam = OrthographicCamera()
        shape = ShapeRenderer()
        player.pos.set(100f, 256f)
        for (x in 0..4095) {
            for (y in 0..192) {
                world[x, y] = Block.dirt
            }
        }
        world.addEntity(player)
    }

    override fun render(delta: Float) {
        cam.position.set(Vector3(player.pos, 0f))
        cam.zoom = 1/10f
        cam.update()
        println(player.pos)

        Gdx.gl20.glClearColor(0f, 0f ,0f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        shape.projectionMatrix = cam.combined

        shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.color = Color.WHITE
        for (i in (player.pos.x.toInt()-10)..(player.pos.x.toInt()+10)) {
            for (j in (player.pos.x.toInt()-10)..(player.pos.x.toInt()+10)) {
                val x = i.toFloat()
                val y = j.toFloat()
                if (world[i, j] != null) {
                    shape.rect(x, y, x+1, y+1);
                }
            }
        }

        shape.color = Color.BLUE
        shape.circle(player.pos.x, player.pos.y, 1f);
        shape.end()

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