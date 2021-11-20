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
    private val database = FirebaseDatabase.getInstance("https://tactic-add7c-default-rtdb.europe-west1.firebasedatabase.app/")

    private lateinit var myColor : PLAYER
    private lateinit var onlinePlayerName: String


    private var clickedFromOnline = false
    private var clickedCounter = 0

    private var repeat = false




    override fun startNewGame(firstPlayer: PLAYER?) {
        Log.d("Bugfix", "Start game")
        Game.getInstance().startNewGame(firstPlayer)
        Game.getInstance().setPlayers(lobby.hostPlayerName, lobby.joinPlayerName)
        val sp = PreferenceManager.getDefaultSharedPreferences(Game.getInstance().getGameActivity().getContext())
        onlinePlayerName = sp.getString("PLAYER_NAME", "Anonymous").toString()
        setOnlineListener()
    }



    private fun setOnlineListener() {
        Log.d("Bugfix", "My Name: $onlinePlayerName, ConnString: ${lobby.getConnString()}")
        database.getReference("gameRooms").child(lobby.getConnString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    synchronized (this) {
                        try {
                            val onlineGameTransferObj = snapshot.getValue(OnlineGameTransferObj::class.java) ?: return
                            if(snapshot.value == null)
                                Game.getInstance().gameOver(null)
                            if (onlineGameTransferObj.x == 0 || onlineGameTransferObj.y == 0)
                                return
                            clickedCounter = onlineGameTransferObj.id
                            clickedFromOnline = true
                            if(clickedCounter - 1 == Game.getInstance().getClickCounter())
                                clickedOn(onlineGameTransferObj.x, onlineGameTransferObj.y)
                            repeat = Game.getActualPlayer() == myColor
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
        Log.d("Bugfix", "OnlineGame.clickedOn() $myColor ${Game.getInstance().getActualPlayer()}")
        synchronized(this) {
            when {
                //Ha nálam jön
                myColor == Game.getInstance().getActualPlayer() -> {

                    Game.getInstance().clickedOn(x, y)

                    uploadStep(x, y)


                    Log.d("Bugfix", "OnlineGame clickedOn")
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

    private fun uploadStep(x: Int, y: Int){
        val ogto =
            OnlineGameTransferObj(
                id = Game.getInstance().getClickCounter(),
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
        Log.d("Bugfix", "setLobby: ${this.lobby.getConnString()}")
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

    override fun getMapHeight(): Int = Game.getInstance().getMapHeight()

    fun setMapSize(width: Int, height: Int){
        Game.getInstance().setMapSize(width, height)
    }

    override fun closeGameRoom(){
        database.getReference("gameRooms").child(lobby.getConnString()).removeValue()
    }

    override fun getFirstPlayer(): PLAYER {
        return lobby.firstPlayer
    }
}