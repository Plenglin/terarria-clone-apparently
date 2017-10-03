package io.github.plenglin.platform.physics.landed.entity

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.sun.javafx.geom.Vec2d
import io.github.plenglin.platform.Block
import io.github.plenglin.platform.physics.landed.Chunk
import io.github.plenglin.platform.physics.landed.World


abstract class LandedEntity {

    /**
     * Its position at its foot.
     */
    val pos: Vector2 = Vector2(0f, 0f)

    /**
     * How fast it is moving relative to the planet.
     */
    val velocity: Vector2 = Vector2(0f, 0f)

    var mass: Float = 0f

    var hasLanded: Boolean = false

    fun canCollideWith(other: LandedEntity): Boolean {
        return false
    }

    abstract fun isCollidingWith(other: LandedEntity): Boolean

    val chunk get(): Chunk? = world?.coordsToChunk(pos)

    fun remove() {
        world!!.entities.remove(this)
        world = null
    }

    var world: World? = null

    fun findCollisions(alreadyTested: List<LandedEntity> = listOf()): List<LandedEntity> {
        val ents = mutableListOf<LandedEntity>()

        val toTest = chunk?.surroundingChunks()?.flatMap { it.entities }

        toTest?.forEach {
            if (it in alreadyTested || it == this || !this.canCollideWith(it)) {
                return@forEach
            }
            if (this.isCollidingWith(it)) {
                ents.add(it)
            }
        }
        return ents
    }

    fun getBlockAtFoot(): Block? {
        return world?.get(pos.x.toInt(), pos.y.toInt())
    }

}

abstract class FreeMovingEntity(val radius: Float) : LandedEntity() {
    override fun isCollidingWith(other: LandedEntity): Boolean {
        when (other) {
            is FreeMovingEntity -> return this.pos.cpy().sub(other.pos).len2() <= radius*radius
            is PlatformableEntity -> return other.isCollidingWith(this)
        }
        return false
    }

    var rotation: Float = 0f

}

abstract class PlatformableEntity(val width: Float, val height: Float) : LandedEntity() {

    fun Rectangle.corners(): List<Vector2> {
        return listOf(
                Vector2(x, y),
                Vector2(x + width, y),
                Vector2(x + width, y + height),
                Vector2(x, y + height)
        )
    }

    override fun isCollidingWith(other: LandedEntity): Boolean {
        when (other) {
            is PlatformableEntity -> return other.rectangle.overlaps(this.rectangle)
            is FreeMovingEntity -> {
                
                val rect = this.rectangle
                
                // Is the center inside the rectangle?
                if (rect.contains(other.pos)) {
                    return true
                }
                
                // Are any of the corners in the circle?
                if (rect.corners().any { it.sub(other.pos).len2() > other.radius*other.radius }) {
                    return true
                }

                // Is the circle's center horizontal to the rectangle?
                if (other.pos.x in rect.x..(rect.x + rect.width)) {
                    return Math.abs(other.pos.x - rect.x) < other.radius || Math.abs(other.pos.x - rect.x - rect.width) < other.radius
                }

                // Is the circle's center horizontal to the rectangle?
                if (other.pos.y in rect.y..(rect.y + rect.height)) {
                    return Math.abs(other.pos.y - rect.y) < other.radius || Math.abs(other.pos.y - rect.y - rect.height) < other.radius
                }
            }
        }
        return false
    }

    val rectangle get(): Rectangle {
        return Rectangle(pos.x, pos.y, width, height)
    }

    var landed: Boolean = false

}
