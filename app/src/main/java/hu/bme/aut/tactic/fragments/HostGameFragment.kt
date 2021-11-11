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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.databinding.HostGameFragmentBinding
import hu.bme.aut.tactic.model.*
import kotlin.concurrent.thread

class HostGameFragment: Fragment() {
    private lateinit var binding: HostGameFragmentBinding
    private lateinit var database: FirebaseDatabase
    private var onlineHostLobby: OnlineHostLobby? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HostGameFragmentBinding.inflate(LayoutInflater.from(context))

        binding.btnHost.setOnClickListener { host() }
        binding.btnCancel.setOnClickListener { cancel() }

        binding.btnCancel.isEnabled = true

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        cancel()
    }


    fun setDatabase(db: FirebaseDatabase) {
        database = db
    }

    private fun host() {
        if (binding.etHostGameName.text.isEmpty())
            return

        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val width = sp.getInt("MAP_WIDTH_VAL", 5)
        val height = sp.getInt("MAP_HEIGHT_VAL", 5)
        val playerName = sp.getString("PLAYER_NAME", "host player").toString()
        onlineHostLobby = OnlineHostLobby(binding.etHostGameName.text.toString(), playerName, width, height)
        database.getReference("lobbies").child("${onlineHostLobby?.lobbyName}")
            .setValue(onlineHostLobby)
        //database.getReference("gameRooms").child("${onlineHostLobby?.getConnString()}")
          //  .setValue(false)
        database.getReference("gameRooms").child("${onlineHostLobby?.getConnString()}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Bugfix", "Snapshot.child(redName)\t${snapshot.child("redName").value}")

                    if(snapshot.child("redName").value != null) {
                        Log.d("Bugfix", "1")
                        val onlineGameTransferObj = OnlineGameTransferObj(snapshot.child("redName").toString())
                        onlineGameTransferObj.setBlueName(sp.getString("PLAYER_NAME", "Blue Player").toString())

                        val editor: SharedPreferences.Editor = sp.edit()
                        editor.putBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", false)
                        editor.apply()


                        Game.getInstance().setOnlineGameTransferObj(onlineGameTransferObj)
                        val intent = Intent(requireContext(), GameActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Bugfix", "Failed to read value.", error.toException())
                }
            })
        binding.btnHost.isEnabled = false
        binding.btnCancel.isEnabled = true
        binding.etHostGameName.isEnabled = false
    }

    private fun cancel() {
        database.getReference("gameRooms").child("${onlineHostLobby?.lobbyName}").removeValue()
        database.getReference("lobbies").child("${onlineHostLobby?.lobbyName}").removeValue()
        binding.btnHost.isEnabled = true
        binding.btnCancel.isEnabled = false
        binding.etHostGameName.isEnabled = true
    }
}