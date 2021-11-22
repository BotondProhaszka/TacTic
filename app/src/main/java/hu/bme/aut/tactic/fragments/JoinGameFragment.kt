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
        OnlineGame.setLobby(onlineHostLobby)
        database.getReference("lobbies").child(onlineHostLobby.lobbyName).setValue(onlineHostLobby)
        OnlineGame.setMyColor(PLAYER.RED)
        OnlineGame.setMapSize(onlineHostLobby.width, onlineHostLobby.height)
        Log.d("Bugfix", "Clicked2: ${onlineHostLobby.getConnString()}")

        val onlineGameTransferObj = OnlineGameTransferObj(0, 0, 0)

        database.getReference("gameRooms").child(onlineHostLobby.getConnString()).setValue(onlineGameTransferObj)


        val intent = Intent(requireContext(), GameActivity::class.java)
        intent.putExtra("isOnline", true)
        startActivity(intent)
    }

}