package hu.bme.aut.tactic.interfaces

import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.fragments.MapView
import hu.bme.aut.tactic.model.Field
import hu.bme.aut.tactic.model.PLAYER

interface GameInterface {
    fun clickedOn(x: Int, y: Int)
    fun getBluePlayersName() : String
    fun getRedPlayersName() : String
    fun setGameActivity(gameActivity: GameActivity)
    fun startNewGame(firstPlayer: PLAYER?)
    fun getScore() : Score
    fun getActualPlayer(): PLAYER
    fun getMap(): ArrayList<ArrayList<Field>>
    fun setMapView(mapView: MapView)
    fun getMapWidth() : Int
    fun closeGameRoom()
    fun getFirstPlayer() : PLAYER?
    fun gameOver(winner: PLAYER?)
    fun isOnline(): Boolean
}