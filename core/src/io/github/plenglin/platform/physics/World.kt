package io.github.plenglin.platform.physics

import com.badlogic.gdx.math.Vector2
import io.github.plenglin.platform.Block

/**
 * A planet.
 * @param circumference how many chunkGrid wide the world is
 */
class World(val circumference: Int) {

    val chunkGrid = Array<Array<Chunk>>(circumference, { x -> Array(worldHeight, { y -> Chunk(this, x, y)})})

    fun chunks(): Iterable<Chunk> = chunkGrid.flatten()

    fun entities() = chunks().flatMap { it.entities }

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
        return chunkGrid[i][j]
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

}

fun main(args: Array<String>) {
    val w = World(128)
}
