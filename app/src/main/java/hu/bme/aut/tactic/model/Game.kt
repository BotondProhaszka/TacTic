package hu.bme.aut.tactic.model


import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import hu.bme.aut.tactic.interfaces.GameInterface
import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.fragments.MapView
import java.lang.Exception

object Game: GameInterface {
    private val instance = Game

    private lateinit var map: ArrayList<ArrayList<Field>>

    private lateinit var gameActivity: GameActivity
    private lateinit var sp: SharedPreferences
    private lateinit var mapView: MapView
    private var clickedFrom: Field? = null

    private var clickCounter = 0


    private var mapWidth = 0
    private var mapHeight = 0
    private var round: ROUND = ROUND.FIRST_BASE
    private var actual_player: PLAYER = PLAYER.BLUE
    private lateinit var bluePlayer: PlayerUser
    private lateinit var redPlayer: PlayerUser
    //private var score = Score(null, "Player1Name", 1, "Player2Name", 2, true)

    fun getInstance(): Game = instance

    fun setPlayers(blue_name: String, red_name: String) {
        bluePlayer = PlayerUser(blue_name, PLAYER.BLUE)
        redPlayer = PlayerUser(red_name, PLAYER.RED)
    }

    override fun getBluePlayersName() : String = bluePlayer.name.toString()
    override fun getRedPlayersName() : String = redPlayer.name.toString()

    fun getActualPlayerUser() : PlayerUser {
        return if(actual_player == PLAYER.BLUE)
            bluePlayer
        else
            redPlayer
    }

    override fun startNewGame(firstPlayer: PLAYER?) {

        if(firstPlayer == null)
            setRandomFirstPlayer()
        else
            actual_player = firstPlayer

        round = ROUND.FIRST_BASE
        val spr = PreferenceManager.getDefaultSharedPreferences(gameActivity.getContext())
        val x = spr.getInt("MAP_WIDTH_VAL", 5)
        val y = spr.getInt("MAP_HEIGHT_VAL", 5)

        setMap(x, y)
    }

    override fun getScore(): Score {
        return Score(null, bluePlayer.name, bluePlayer.score, redPlayer.name, redPlayer.score, true, winner = null)
    }

    fun restartGame() {
        round = ROUND.FIRST_BASE
        setMap(mapWidth, mapHeight)
        setRandomFirstPlayer()
        bluePlayer.score = 0
        redPlayer.score = 0
    }

    private fun setMap(width: Int, height: Int) {

        mapWidth = width
        mapHeight = height
        map = ArrayList()
        for (i in 0..width) {
            val row = ArrayList<Field>(height)
            for (j in 0..height) {
                row.add(Field(i, j))
            }
            map.add(row)
        }
    }

    private fun setRandomFirstPlayer() {
        val rand = (0..1).random()
        actual_player = when (rand) {
            0 -> PLAYER.BLUE
            1 -> PLAYER.RED
            else -> PLAYER.RED
        }
    }

    override fun setGameActivity(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
    }

    fun getGameActivity() : GameActivity = gameActivity

    override fun getMap(): ArrayList<ArrayList<Field>> {
        return map
    }

    override fun getActualPlayer(): PLAYER {
        return actual_player
    }

    override fun getMapWidth(): Int {
        return mapWidth
    }

    override fun getMapHeight(): Int {
        return mapHeight
    }

    override fun closeGameRoom() { }
    override fun getFirstPlayer(): PLAYER? {
        return null
    }

    fun setSharedPreferences(sp: SharedPreferences) {
        this.sp = sp
    }

    override fun setMapView(mapView: MapView){
        this.mapView = mapView
    }

    fun getClickCounter() : Int = clickCounter


    fun setMapSize(x: Int, y: Int){
        mapWidth = x
        mapHeight = y
    }

    private fun playerStepped() {
            actual_player = getOtherPlayer(actual_player)
            removeClickedFrom()
            mapView.invalidate()
    }

    private fun getOtherPlayer(player: PLAYER): PLAYER {
        return when (player) {
            PLAYER.RED -> PLAYER.BLUE
            PLAYER.BLUE -> PLAYER.RED
        }
    }

    fun gameOver(winner: PLAYER?) {
        OnlineGame.gameOver()
        val score = Score(
            id = null,
            player1Name = bluePlayer.name,
            player1Score = bluePlayer.score,
            player2Name = redPlayer.name,
            player2Score = redPlayer.score,
            offlineGame = true,
            winner = winner )
        Log.i("Bugfix", "$winner")
        restartGame()
        gameActivity.gameOver(score)
    }

    override fun clickedOn(x: Int, y: Int) {
        if (round != ROUND.FIRST_BASE && round != ROUND.SEC_BASE)
            if (!hasCorrectNeighbour(x, y) && actual_player != map[x][y].getPlayer()) {
                if (clickedFrom != null)
                    removeClickedFrom()
                return
            }
        clickCounter++
        when (round) {
            ROUND.FIRST_BASE -> {
                map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                playerStepped()
                round = ROUND.SEC_BASE
                return
            }
            ROUND.SEC_BASE -> {
                map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                playerStepped()
                round = ROUND.GAME
                return
            }
            ROUND.GAME -> {
                if (map[x][y].getPlayer() == null) {
                    map[x][y] = Field(x, y, actual_player, SIGN.ONE, false)
                    conquestAddScore()
                    playerStepped()
                    return
                } else if (clickedFrom == null) {
                    if (map[x][y].getPlayer() == actual_player) {
                        map[x][y].setHighlighted(true)
                        clickedFrom = map[x][y]
                        return
                    }
                } else if (clickedFrom?.samePlace(map[x][y]) == true) {
                    if (map[x][y].increaseSignValue()) {
                        increaseAddScore()
                        playerStepped()
                    }
                    return
                } else if (clickedFrom?.getPlayer() == map[x][y].getPlayer()) {
                    clickedFrom?.setHighlighted(false)
                    map[x][y].setHighlighted(true)
                    clickedFrom = map[x][y]
                    return
                } else {
                    if (map[x][y].attackFrom(Field(clickedFrom))) {
                        try {
                            Log.d("Bugfix", "ASd ${map[x][y].sign}")
                            val mapS = map[x][y].sign
                            val s = mapS?.let { clickedFrom!!.sign?.getMinus(it) }
                            if (s != null) {
                                clickedFrom?.setSign(s)
                            }

                        } catch (e: Exception){
                        }
                        playerStepped()
                        return
                    }
                }
            }
        }
    }

    private fun hasCorrectNeighbour(x: Int, y: Int): Boolean {
        if (x in 2..mapWidth)
            if (map[x - 1][y].getPlayer() == actual_player)
                return true
        if (x in 1 until mapWidth)
            if (map[x + 1][y].getPlayer() == actual_player)
                return true
        if (y in 2..mapHeight)
            if (map[x][y - 1].getPlayer() == actual_player)
                return true
        if (y in 1 until mapHeight)
            if (map[x][y + 1].getPlayer() == actual_player)
                return true
        return false
    }

    private fun removeClickedFrom() {
        clickedFrom?.setHighlighted(false)
        clickedFrom = null
    }

    private fun conquestAddScore(){
        getActualPlayerUser().score += SCORE_CONQUEST
    }

    private fun increaseAddScore(){
        getActualPlayerUser().score += SCORE_INCREASE
    }

    fun attackAddScore(score: Int){
        getActualPlayerUser().score += score
    }

}