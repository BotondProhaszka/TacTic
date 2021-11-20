package hu.bme.aut.tactic.model

import android.util.Log
import hu.bme.aut.tactic.interfaces.GameInterface

object MapViewHelper {
    var game: GameInterface = Game.getInstance()
        get(){
            Log.d("Bugfix", "MapViewHelper return: $field")
            return field
        }
        set(game: GameInterface){
            field = game
            Log.d("Bugfix", "MapViewHelper set: $game, $field")
        }
}