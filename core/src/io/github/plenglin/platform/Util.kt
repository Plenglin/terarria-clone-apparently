package io.github.plenglin.platform

/**
 *
 */
object Util {

    fun floorMod(x: Float, n: Float): Float {
        return (x % n + n) % n
    }

}