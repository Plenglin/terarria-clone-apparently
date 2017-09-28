package io.github.plenglin.platform.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor

/**
 *
 */
object InputHelper : InputProcessor {

    private val whilePressed: MutableMap<Int, (Float) -> Unit> = mutableMapOf()
    private val onKeyDown: MutableMap<Int, () -> Boolean> = mutableMapOf()
    private val onKeyUp: MutableMap<Int, () -> Boolean> = mutableMapOf()

    fun whilePressed(key: Int, cb: (Float) -> Unit) {
        whilePressed.put(key, cb)
    }

    fun removeWhilePressed(key: Int) {
        whilePressed.remove(key)
    }

    fun onKeyDown(key: Int, cb: () -> Boolean) {
        onKeyDown.put(key, cb)
    }
    
    fun onKeyUp(key: Int, cb: () -> Boolean) {
        onKeyUp.put(key, cb)
    }

    fun onKeyUp(vararg key: Int) {

    }

    fun update(delta: Float) {
        whilePressed.forEach { k, cb -> if (Gdx.input.isKeyPressed(k)) {cb(delta)} }
    }

    override fun keyDown(keycode: Int): Boolean {
        for ((k, cb) in onKeyDown) {
            if (keycode == k && cb()) {
                return true
            }
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        for ((k, cb) in onKeyUp) {
            if (keycode == k && cb()) {
                return true
            }
        }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

}