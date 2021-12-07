package hu.bme.aut.tactic.model

import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.bme.aut.tactic.interfaces.GameInterface
import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.fragments.MapView

object OnlineGame : GameInterface {

    private lateinit var lobby: OnlineHostLobby
    private val database = FirebaseDatabase.getInstance(FIREBASE_CONN_STRING)

    private lateinit var myColor : PLAYER
    private lateinit var onlinePlayerName: String
    private var clickedFromOnline = false
    private var clickedCounter = 0

    private var repeat = false


    override fun startNewGame(firstPlayer: PLAYER?) {
        Game.getInstance().startNewGame(firstPlayer)
        Game.getInstance().setPlayers(lobby.hostPlayerName, lobby.joinPlayerName)
        val sp = PreferenceManager.getDefaultSharedPreferences(Game.getInstance().getGameActivity().getContext())
        onlinePlayerName = sp.getString(SP_PLAYER_NAME, "Anonymous").toString()
        setOnlineListener()
    }

    private fun setOnlineListener() {
        database.getReference("gameRooms").child(lobby.getConnString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    synchronized (this) {
                        try {
                            val onlineGameTransferObj = snapshot.getValue(OnlineGameTransferObj::class.java)
                            if(onlineGameTransferObj == null){
                                gameOver(null)
                                return
                            }
                            else {
                                if (onlineGameTransferObj.x == 0 || onlineGameTransferObj.y == 0)
                                    return
                                clickedCounter = onlineGameTransferObj.id
                                clickedFromOnline = true
                                if (clickedCounter - 1 == Game.getInstance().getClickCounter()) {
                                    clickedOn(onlineGameTransferObj.x, onlineGameTransferObj.y)
                                } else { }
                            }
                        } catch (e: Exception) {
                            Log.e("Bugfix", "OnlineGame listener: ${e.message}")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Bugfix", "Failed to read value.", error.toException())
                }
            })
    }

    override fun clickedOn(x: Int, y: Int) {
        synchronized(this) {
            when {
                //Ha nálam jön
                myColor == Game.getInstance().getActualPlayer() -> {
                    uploadStep(x, y)
                    Game.getInstance().clickedOn(x, y)
                }
                //Ha online jött és még nem léptem le
                clickedFromOnline && (clickedCounter - 1 == Game.getInstance().getClickCounter()) -> {
                    clickedFromOnline = false
                    Game.getInstance().clickedOn(x, y)
                }
                else -> return
            }
        }
    }


    override fun gameOver(winner: PLAYER?){
        synchronized(this) {
            if (Game.isGameOver())
                return
            Game.gameOver(winner)
        }
    }

    override fun isOnline(): Boolean = true

    private fun uploadStep(x: Int, y: Int){
        val ogto =
            OnlineGameTransferObj(
                id = Game.getInstance().getClickCounter() + 1,
                x = x,
                y = y
            )
        database.getReference("gameRooms").child(lobby.getConnString()).setValue(ogto)
    }


    override fun setGameActivity(gameActivity: GameActivity) {
        Game.getInstance().setGameActivity(gameActivity)
    }

    override fun setMapView(mapView: MapView) {
        Game.getInstance().setMapView(mapView)
    }

    fun setMyColor(color: PLAYER){
        myColor = color
    }

    fun setLobby(lobby: OnlineHostLobby){
        this.lobby = lobby
    }

    override fun getBluePlayersName(): String = lobby.hostPlayerName

    override fun getRedPlayersName(): String = lobby.joinPlayerName

    override fun getScore(): Score {
        val score = Game.getInstance().getScore()
        score.offlineGame = false
        return score
    }
    override fun getActualPlayer(): PLAYER = Game.getInstance().getActualPlayer()

    override fun getMap(): ArrayList<ArrayList<Field>> = Game.getInstance().getMap()


    override fun getMapWidth(): Int = Game.getInstance().getMapWidth()


    fun setMapSize(size: Int){
        Game.getInstance().setMapSize(size)
    }

    override fun closeGameRoom(){
        database.getReference("gameRooms").child(lobby.getConnString()).removeValue()
    }

    override fun getFirstPlayer(): PLAYER {
        return lobby.firstPlayer
    }
}