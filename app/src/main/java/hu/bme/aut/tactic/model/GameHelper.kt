package hu.bme.aut.tactic.model

import hu.bme.aut.tactic.interfaces.GameInterface

object GameHelper {
    var game: GameInterface = Game.getInstance()
}