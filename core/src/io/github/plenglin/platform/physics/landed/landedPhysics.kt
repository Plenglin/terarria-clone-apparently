package io.github.plenglin.platform.physics.landed

import com.badlogic.gdx.math.Vector2
import io.github.plenglin.platform.Block
import io.github.plenglin.platform.physics.landed.entity.LandedEntity

/**
 * A planet.
 * @param circumference how many chunks wide the world is
 */
class World(val circumference: Int) {

    val chunks = Array<Array<Chunk>>(circumference, { x -> Array(worldHeight, { y -> Chunk(this, x, y) })})

    internal val collisionListeners = mutableListOf<(Collision) -> Unit>()
    internal val entities: MutableList<LandedEntity> = mutableListOf()

    fun wrapCoords(x: Int): Int {
        return Math.floorMod(x, circumference * Chunk.length)
    }

    /**
     * Get the chunk that the given coordinates lie in. Origin is at prime meridian and magma.
     * @param x the x. Accounts for the round planet.
     * @param y the y.
     * @return the chunk.
     */
    fun coordsToChunk(x: Int, y: Int): Chunk {
        val i = wrapCoords(x) / Chunk.length
        val j = y / Chunk.length
        return chunks[i][j]
    }

    fun coordsToChunk(x: Number, y: Number): Chunk {
        return coordsToChunk(x.toInt(), y.toInt())
    }

    fun coordsToChunk(p: Vector2): Chunk {
        return coordsToChunk(p.x, p.y)
    }

    operator fun get(x: Int, y: Int): Block? {
        val ch = coordsToChunk(x, y)
        return ch[Math.floorMod(x, Chunk.length), Math.floorMod(y, Chunk.length)]
    }

    operator fun set(x: Int, y: Int, v: Block?) {
        val ch = coordsToChunk(x, y)
        ch[Math.floorMod(x, Chunk.length), Math.floorMod(y, Chunk.length)] = v
    }

    companion object {
        val worldHeight = 16
    }

    fun update(delta: Float) {
        val collisions = getCollisions()
        for (c in collisions) {
            collisionListeners.forEach { it(c) }
        }
    }

    fun getCollisions(): List<Collision> {
        val tested = mutableMapOf<LandedEntity, MutableList<LandedEntity>>().withDefault { mutableListOf() }
        val collisions = mutableListOf<Collision>()
        for (e in entities) {
            val collidesWith = e.findCollisions(tested[e]!!)
            for (o in collidesWith) {
                collisions += Collision(e, o)
                tested.put(o, (tested[o]!! + collidesWith).toMutableList())
            }
        }
        return collisions
    }

    fun addEntity(entity: LandedEntity) {
        entities += entity
        entity.world = this
    }

}

class Collision(val a: LandedEntity, val b: LandedEntity) {

    override fun equals(other: Any?): Boolean {
        if (other is Collision) {
            return (this.a == other.a && this.b == other.b) || (this.a == other.b && this.b == other.a)
        }
        return false
    }

    override fun hashCode(): Int {
        var result = a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }

}

class Chunk(val world: World, val worldI: Int, val worldJ: Int) {

    private val grid = Array<Array<Block?>>(64, { Array<Block?>(64, {null}) })

    internal val entities = mutableListOf<LandedEntity>()

    fun surroundingChunks(radius: Int = 1): List<Chunk> {
        val chunks = mutableListOf<Chunk>()
        for (i in (worldI - radius)..(worldI + radius)) {
            for (j in (worldJ - radius)..(worldJ + radius)) {
                try {
                    val c = world.chunks[i][j]
                    if (this != c) {
                        chunks += c
                    }
                } catch (_: ArrayIndexOutOfBoundsException) {

                }
            }
        }
        return chunks
    }

    operator fun get(i: Int): Array<Block?> {
        return grid[i]
    }

    operator fun get(i: Int, j: Int): Block? {
        return grid[i][j]
    }

    operator fun set(i: Int, j: Int, b: Block?) {
        grid[i][j] = b
    }

    companion object {

        val length = 64

    }

}
