package hu.bme.aut.tactic.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.bme.aut.tactic.adapters.JoinGameAdapter
import hu.bme.aut.tactic.databinding.JoinGameFragmentBinding
import kotlin.concurrent.thread
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot
import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.model.*
import kotlin.random.Random


class JoinGameFragment: Fragment(), JoinGameAdapter.JoinGameClickListener{
    private lateinit var binding: JoinGameFragmentBinding
    private lateinit var adapter: JoinGameAdapter
    private var database = FirebaseDatabase.getInstance("https://tactic-add7c-default-rtdb.europe-west1.firebasedatabase.app/")
    private var games : ArrayList<OnlineHostLobby> = ArrayList()

    private var firstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JoinGameFragmentBinding.inflate(LayoutInflater.from(context))
        initRecyclerView()

        binding.btnReload.setOnClickListener {
            getRooms()
        }

        database.getReference("gameRooms").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                getRooms()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        return binding.root
    }

    fun setDatabase(db: FirebaseDatabase){
        database = db
    }

    private fun initRecyclerView(){
        adapter = JoinGameAdapter(this)
        binding.rvRooms.layoutManager = LinearLayoutManager(this.context)
        binding.rvRooms.adapter = adapter
        getRooms()
    }

    private fun loadRoomsInBackground(){
        val list = games.toMutableList()
        activity?.runOnUiThread {
            adapter.update(list)
        }
    }

    private fun getRooms() {
        adapter.clear()
        games.clear()
        database.getReference("lobbies").get().addOnSuccessListener {
            if (!firstLoad)
                for (a in it.children) {
                    try {
                        val ohl = a.getValue(OnlineHostLobby::class.java)!!
                        games.add(ohl)
                        loadRoomsInBackground()
                    }catch (e: Exception){
                        Log.e("Bugfix", "JoinGameFragment getRooms: ${e.message}")
                    }
                }
            else
                firstLoad = false
        }.addOnFailureListener {
            Log.e("Bugfix", "Error getting data", it)
        }

    }

    override fun onOnlineHostLobbyClicked(onlineHostLobby: OnlineHostLobby) {

        Log.d("Bugfix", "Clicked: ${onlineHostLobby.getConnString()}")
        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        onlineHostLobby.joinPlayerName = sp.getString("PLAYER_NAME", "Red Player").toString()
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putInt("MAP_WIDTH_VAL", onlineHostLobby.width)
        editor.putInt("MAP_HEIGHT_VAL", onlineHostLobby.height)
        editor.apply()
        Game.getInstance().setSharedPreferences(sp)
        OnlineGame.getInstance().setLobby(onlineHostLobby)
        database.getReference("lobbies").child(onlineHostLobby.lobbyName).setValue(onlineHostLobby)
        OnlineGame.getInstance().setMyColor(PLAYER.RED)
        OnlineGame.getInstance().setMapSize(onlineHostLobby.width, onlineHostLobby.height)
        Log.d("Bugfix", "Clicked2: ${onlineHostLobby.getConnString()}")

        val onlineGameTransferObj = OnlineGameTransferObj(0, 0, 0)

        database.getReference("gameRooms").child(onlineHostLobby.getConnString()).setValue(onlineGameTransferObj)


        val intent = Intent(requireContext(), GameActivity::class.java)
        intent.putExtra("isOnline", true)
        startActivity(intent)

        /*
        onlineGameTransferObj.blueName = onlineHostLobby.hostPlayerName
        OnlineGame.getInstance().setOnlinePlayerName(onlineGameTransferObj.redName)

        val randFirstPlayer = when ((0..1).random()) {
            0 -> onlineGameTransferObj.blueName
            1 -> onlineGameTransferObj.redName
            else -> onlineGameTransferObj.blueName
        }
        onlineGameTransferObj.lastPlayer = randFirstPlayer


        database.getReference("gameRooms").child(onlineHostLobby.getConnString()).child("ogto").setValue(onlineGameTransferObj)
        database.getReference("lobbies").child(onlineHostLobby.lobbyName).removeValue()



        if(onlineGameTransferObj.blueName == onlineGameTransferObj.lastPlayer)
            Game.getInstance().setFirstPlayer(PLAYER.RED)
        else
            Game.getInstance().setFirstPlayer(PLAYER.BLUE)

        Game.getInstance().setOnlineGameTransferObj(onlineGameTransferObj)
        Game.getInstance().setOnlineHostLobby(onlineHostLobby)
        Game.getInstance().isOnline(true)
        Game.getInstance().startNewGame()

        val editor: SharedPreferences.Editor = sp.edit()
        editor.putBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", false)
        editor.apply()

        val intent = Intent(requireContext(), GameActivity::class.java)
        intent.putExtra("isOnline", true)
        startActivity(intent)
         */

    }

}