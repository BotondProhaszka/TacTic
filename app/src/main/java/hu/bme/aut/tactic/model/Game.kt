package hu.bme.aut.tactic.model


import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.fragments.MapView
import java.lang.Exception
import java.security.spec.ECField

enum class ROUND {INIT,FIRST_BASE, SEC_BASE, GAME, DRAW, BLUE_WIN, RED_WIN}

enum class PLAYER {
    RED, BLUE

}

enum class SIGN (val value: Int) {
    BASE(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);
    fun getMinus(rightSide: SIGN): SIGN {
        return when(this.value - rightSide.value){
            0 -> ONE
            1 -> ONE
            2 -> TWO
            3 -> THREE
            4 -> FOUR
            5 -> FIVE
            else -> ONE
        }
    }
    fun minus(rightSide: SIGN): Int {
        return this.value - rightSide.value
    }

    fun getNext() : SIGN {
        return when(value){
            0 -> ONE
            1 -> TWO
            2 -> THREE
            3 -> FOUR
            4 -> FIVE
            5 -> SIX
            else -> BASE
        }
    }
}

object Game {
    private var instance = Game

    private var isOnline = false
    private lateinit var onlinePlayerName: String
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var map: ArrayList<ArrayList<Field>>

    private var mapWidth = 0
    private var mapHeight = 0
    private var round: ROUND = ROUND.FIRST_BASE
    private var actual_player: PLAYER = PLAYER.BLUE
    private var bluePlayer: PlayerUser? = null
    private var redPlayer: PlayerUser? = null

    private var score = Score(null, "Player1Name", 1, "Player2Name", 2, true)

    private var clickedFrom: Field? = null

    private lateinit var gameActivity: GameActivity


    private lateinit var onlineGameTransferObj: OnlineGameTransferObj
    private lateinit var lobby: OnlineHostLobby

    private lateinit var sp: SharedPreferences

    private lateinit var mapView: MapView

    var clickedFromOnline = false

    fun getInstance(): Game {
        return instance
    }

    fun setPlayers(blue_name: String, red_name: String) {
        bluePlayer = PlayerUser(blue_name, PLAYER.BLUE)
        redPlayer = PlayerUser(red_name, PLAYER.RED)
    }

    fun getBluePlayersName(): String {
        if (isOnline)
            return onlineGameTransferObj.blueName
        if (bluePlayer != null)
            return bluePlayer?.name.toString()
        return ""

    }

    fun getRedPlayersName(): String {
        if (isOnline)
            return onlineGameTransferObj.redName
        if (redPlayer != null)
            return redPlayer?.name.toString()
        return ""

    }

    fun startNewGame() {
        synchronized(this) {
            if (isOnline) {
                setMap(lobby.width, lobby.height)
                round = ROUND.FIRST_BASE
                firebaseDatabase =
                    FirebaseDatabase.getInstance("https://tactic-add7c-default-rtdb.europe-west1.firebasedatabase.app/")
                if (actual_player == PLAYER.BLUE)
                    onlineGameTransferObj.lastPlayer = onlineGameTransferObj.redName
                else
                    onlineGameTransferObj.lastPlayer = onlineGameTransferObj.blueName
                setOnlineListener()
            } else {
                setRandomFirstPlayer()
                val spr = PreferenceManager.getDefaultSharedPreferences(gameActivity.getContext())
                val x = spr.getInt("MAP_WIDTH_VAL", 5)
                val y = spr.getInt("MAP_HEIGHT_VAL", 5)
                round = ROUND.FIRST_BASE
                setMap(x, y)
            }
            Log.d("Bugfix", "First player: $actual_player")
        }
    }

    fun restartGame() {
        round = ROUND.FIRST_BASE
        setMap(mapWidth, mapHeight)
        setRandomFirstPlayer()

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

    fun setFirstPlayer(player : PLAYER){
        actual_player = player
    }
    fun setGameActivity(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
    }

    @JvmName("getMap1")
    fun getMap(): ArrayList<ArrayList<Field>> {
        return map
    }

    fun getActualPlayer(): PLAYER {
        return actual_player
    }

    fun getMapWidth(): Int {
        return mapWidth
    }

    fun getMapHeight(): Int {
        return mapHeight
    }

    fun getScore(): Score {
        return score
    }

    fun isOnline(): Boolean = isOnline
    fun isOnline(isOnline: Boolean) {
        this.isOnline = isOnline
    }

    fun getOnlineGameTransferObj(): OnlineGameTransferObj = onlineGameTransferObj
    fun setOnlineGameTransferObj(onlineGameTransferObj: OnlineGameTransferObj) {
        this.onlineGameTransferObj = onlineGameTransferObj

    }

    fun getOnlineHostLobby(): OnlineHostLobby = lobby
    fun setOnlineHostLobby(onlineHostLobby: OnlineHostLobby) {
        lobby = onlineHostLobby
    }

    fun getOnlinePlayerName(): String = onlinePlayerName
    fun setOnlinePlayerName(name: String) {
        onlinePlayerName = name
    }

    fun setSharedPreferences(sp: SharedPreferences) {
        this.sp = sp
    }

    fun clearDatabaseAfterGame(){
        if(isOnline)
            firebaseDatabase.getReference("gameRooms").child(lobby.getConnString()).removeValue()
    }

    fun setMapView(view: MapView){
        this.mapView = view
    }

    fun setOnlineListener() {
        firebaseDatabase.getReference("gameRooms").child(lobby.getConnString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    synchronized(this) {
                        if (snapshot.value == null) {
                            gameOverDraw()
                            return
                        }
                        if(snapshot.child("readable").value == true) {
                            firebaseDatabase.getReference("gameRooms").child(lobby.getConnString()).child("readable").setValue(false)
                            onlineChange(snapshot)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Bugfix", "Failed to read value.", error.toException())
                }
            })
    }

    private fun onlineChange(snapshot: DataSnapshot){
        if (snapshot.child("readable").value == false)
            return

        firebaseDatabase.getReference("gameRooms").child(lobby.getConnString()).child("readable").setValue(false)

        onlineGameTransferObj = snapshot.child("ogto").getValue(OnlineGameTransferObj::class.java) ?: return

        if(onlineGameTransferObj.lastPlayer == onlinePlayerName)
            return


        if (onlineGameTransferObj.plsStep == 1) {
            onlineGameTransferObj.plsStep = 0;
            clickedFromOnline = true
            clickedOn(onlineGameTransferObj.x, onlineGameTransferObj.y)
        }
    }



    private fun playerStepped(x: Int, y: Int) {
        if (isOnline) {
            onlineGameTransferObj.x = x
            onlineGameTransferObj.y = y
            onlineGameTransferObj.lastPlayer = onlinePlayerName
            onlineGameTransferObj.plsStep = 1
            firebaseDatabase.getReference("gameRooms").child(lobby.getConnString()).child("ogto").setValue(onlineGameTransferObj)
            firebaseDatabase.getReference("gameRooms").child(lobby.getConnString()).child("readable").setValue(true)
        }
        if(false){//!clickedFromOnline) {
            actual_player = getOtherPlayer(actual_player)
            removeClickedFrom()
            mapView.invalidate()
        }
    }

    private fun getOtherPlayer(player: PLAYER): PLAYER {
        return when (player) {
            PLAYER.RED -> PLAYER.BLUE
            PLAYER.BLUE -> PLAYER.RED
        }
    }

    fun gameOverDraw(){
        gameOver(null)
    }

    fun gameOver(winner: PLAYER?) {
        clearDatabaseAfterGame()
        restartGame()
        gameActivity.gameOver(score)
    }

    fun clickedOn(x: Int, y: Int){
        synchronized(this){
            clickedOnSyn(x,y)
        }
    }

    fun clickedOnSyn(x: Int, y: Int) {
        if (round != ROUND.FIRST_BASE && round != ROUND.SEC_BASE)
            if (!hasCorrectNeighbour(x, y)) {
                if (clickedFrom != null)
                    removeClickedFrom()
                return
            }

        if(isOnline)
            if(onlineGameTransferObj.lastPlayer == onlinePlayerName && !clickedFromOnline)
                return

        when (round) {
            ROUND.FIRST_BASE -> {
                map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                playerStepped(x, y)
                round = ROUND.SEC_BASE
                return
            }
            ROUND.SEC_BASE -> {
                map[x][y] = Field(x, y, actual_player, SIGN.BASE, false)
                playerStepped(x, y)
                round = ROUND.GAME
                return
            }
            ROUND.GAME -> {

                if (map[x][y].getPlayer() == null) {
                    map[x][y] = Field(x, y, actual_player, SIGN.ONE, false)
                    playerStepped(x, y)
                    return
                } else if (clickedFrom == null) {
                    if (map[x][y].getPlayer() == actual_player) {
                        map[x][y].setHighlighted(true)
                        clickedFrom = map[x][y]
                        return
                    }
                } else if (clickedFrom?.samePlace(map[x][y]) == true) {
                    if (map[x][y].increaseSignValue())
                        playerStepped(x, y)
                    return
                } else if (clickedFrom?.getPlayer() == map[x][y].getPlayer()) {
                    clickedFrom?.setHighlighted(false)
                    map[x][y].setHighlighted(true)
                    clickedFrom = map[x][y]
                    return
                } else {
                    if (map[x][y].attackFrom(Field(clickedFrom))) {
                        try {
                            clickedFrom!!.setSign(map[x][y].getSign()?.let {
                                clickedFrom!!.getSign()?.getMinus(
                                    it
                                )
                            }!!)
                        } catch (e: Exception){
                            Log.e("Bugfix", "Game.attack error: ${e.message}")
                        }
                        playerStepped(x, y)
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

}