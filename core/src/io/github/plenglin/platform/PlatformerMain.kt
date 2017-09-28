package io.github.plenglin.platform

import com.badlogic.gdx.Game

class PlatformerMain : Game() {

    override fun create() {
        setScreen(GameScreen())
    }


}