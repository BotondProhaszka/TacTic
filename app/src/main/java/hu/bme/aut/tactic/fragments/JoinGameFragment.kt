package hu.bme.aut.tactic.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.bme.aut.tactic.adapters.JoinGameAdapter
import hu.bme.aut.tactic.adapters.ScoresAdapter
import hu.bme.aut.tactic.databinding.JoinGameFragmentBinding
import hu.bme.aut.tactic.model.OnlineHostLobby
import kotlin.concurrent.thread
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot




class JoinGameFragment: Fragment(), JoinGameAdapter.JoinGameClickListener{
    private lateinit var binding: JoinGameFragmentBinding
    private lateinit var adapter: JoinGameAdapter
    private lateinit var database: FirebaseDatabase
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

        database.getReference("hostRooms").addValueEventListener(object : ValueEventListener {
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
        database.getReference("hostRooms").get().addOnSuccessListener {
            if (!firstLoad)
                for (a in it.children) {
                    val ohl = OnlineHostLobby(
                        a.child("id").value.toString().toInt(),
                        a.child("lobbyName").value.toString(),
                        a.child("width").value.toString().toInt(),
                        a.child("height").value.toString().toInt()
                    )
                    games.add(ohl)
                    loadRoomsInBackground()
                }
            else
                firstLoad = false
        }.addOnFailureListener {
            Log.e("Bugfix", "Error getting data", it)
        }

    }

    override fun onOnlineHostLobbyClicked(onlineHostLobby: OnlineHostLobby) {
        TODO("Not yet implemented")
    }

}