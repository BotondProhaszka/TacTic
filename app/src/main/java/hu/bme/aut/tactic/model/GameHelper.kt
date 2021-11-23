package hu.bme.aut.tactic.model

import android.util.Log
import hu.bme.aut.tactic.interfaces.GameInterface

object GameHelper {
    var game: GameInterface = Game.getInstance()
}