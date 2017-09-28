package io.github.plenglin.platform.physics

import io.github.plenglin.platform.Block

/**
 *
 */
class Chunk(world: World, val worldX: Int, val worldY: Int) {

    private val grid = Array<Array<Block?>>(64, { Array<Block?>(64, {null}) })

    val entities = mutableListOf<Entity>()

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
