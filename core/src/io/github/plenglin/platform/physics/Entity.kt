package io.github.plenglin.platform.physics

import com.badlogic.gdx.math.Vector2

/**
 *
 */
abstract class Entity {

    val pos: Vector2 = Vector2(0f, 0f)
    val velocity: Vector2 = Vector2(0f, 0f)

    var mass: Float = 0f

    var hasLanded: Boolean = false

    fun canCollideWith(other: Entity): Boolean {
        return false
    }

}