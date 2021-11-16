package hu.bme.aut.tactic.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    ): View {
        binding = HostGameFragmentBinding.inflate(LayoutInflater.from(context))

        binding.btnHost.setOnClickListener { host() }
        binding.btnCancel.setOnClickListener {
            database.getReference("gameRooms").child("${onlineHostLobby?.getConnString()}").removeValue()
            cancel()
        }

        binding.btnCancel.isEnabled = false

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
        if (binding.etHostGameName.text.isEmpty()) {
            Toast.makeText(this.context, "Did you forget to type the name?", Toast.LENGTH_LONG).show()
            return
        }

        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val width = sp.getInt("MAP_WIDTH_VAL", 5)
        val height = sp.getInt("MAP_HEIGHT_VAL", 5)
        val playerName = sp.getString("PLAYER_NAME", "host player").toString()

        onlineHostLobby = OnlineHostLobby(binding.etHostGameName.text.toString(), playerName, width, height)

        database.getReference("lobbies").child("${onlineHostLobby?.lobbyName}")
            .setValue(onlineHostLobby)

        database.getReference("gameRooms").child("${onlineHostLobby?.getConnString()}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value != null) {
                        synchronized(this) {
                            joined(snapshot)
                        }
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
        database.getReference("lobbies").child("${onlineHostLobby?.lobbyName}").removeValue()
        binding.btnHost.isEnabled = true
        binding.btnCancel.isEnabled = false
        binding.etHostGameName.isEnabled = true
    }

    private fun joined(snapshot: DataSnapshot){
        try {
            val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
            val onlineGameTransferObj = snapshot.child("ogto").getValue(OnlineGameTransferObj::class.java)!!

            database.getReference("gameRooms").child("${onlineHostLobby?.getConnString()}").child("ogto").child("blueName").setValue(onlineGameTransferObj.blueName)

            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", false)
            editor.apply()

            if(onlineGameTransferObj.blueName == onlineGameTransferObj.lastPlayer)
                Game.getInstance().setFirstPlayer(PLAYER.RED)
            else
                Game.getInstance().setFirstPlayer(PLAYER.BLUE)

            Game.getInstance().setOnlinePlayerName(onlineGameTransferObj.blueName)

            Game.getInstance().setOnlineGameTransferObj(onlineGameTransferObj)
            Game.getInstance().setOnlineHostLobby(onlineHostLobby!!)
            Game.getInstance().setSharedPreferences(sp)
            Game.getInstance().isOnline(true)
            Game.getInstance().startNewGame()
        } catch (e: Exception) {
            Log.e("Bugfix", "${e.message}")
        }
        val intent = Intent(requireContext(), GameActivity::class.java)
        startActivity(intent)
    }
}