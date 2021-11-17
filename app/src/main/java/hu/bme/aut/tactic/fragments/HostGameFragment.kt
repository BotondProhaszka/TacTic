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

class HostGameFragment: Fragment() {
    private lateinit var binding: HostGameFragmentBinding
    private var database = FirebaseDatabase.getInstance("https://tactic-add7c-default-rtdb.europe-west1.firebasedatabase.app/")
    private lateinit var onlineHostLobby: OnlineHostLobby

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HostGameFragmentBinding.inflate(LayoutInflater.from(context))

        binding.btnHost.setOnClickListener {
            try {
                host()
            } catch (e: Exception) {
                Log.e("Bugfix", "HostGameFragment host: ${e.message}")
            }
        }
        binding.btnCancel.setOnClickListener {
            database.getReference("gameRooms").child(onlineHostLobby.getConnString()).removeValue()
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
        onlineHostLobby = initOnlineHostLobby()
        database.getReference("lobbies").child(onlineHostLobby.lobbyName).setValue(onlineHostLobby)
        initJoinListener()
        binding.btnHost.isEnabled = false
        binding.btnCancel.isEnabled = true
        binding.etHostGameName.isEnabled = false
    }

    private fun cancel() {
        database.getReference("lobbies").child(onlineHostLobby.lobbyName).removeValue()
        binding.btnHost.isEnabled = true
        binding.btnCancel.isEnabled = false
        binding.etHostGameName.isEnabled = true
    }

    private fun initOnlineHostLobby() : OnlineHostLobby{
        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val width = sp.getInt("MAP_WIDTH_VAL", 5)
        val height = sp.getInt("MAP_HEIGHT_VAL", 5)
        val playerName = sp.getString("PLAYER_NAME", "host player").toString()

        val randFirstPlayer = when ((0..1).random()) {
            0 -> PLAYER.BLUE
            1 -> PLAYER.RED
            else -> PLAYER.BLUE
        }
        return OnlineHostLobby(binding.etHostGameName.text.toString(), playerName, width, height, randFirstPlayer)
    }

    private fun initJoinListener(){
        database.getReference("lobbies").child(onlineHostLobby.lobbyName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Bugfix", "initJoinListener: $snapshot")
                    if(snapshot.value != null) {
                        if (snapshot.child("joinPlayerName").value != null) {
                            if (snapshot.child("joinPlayerName").value != "") {
                                synchronized(this) {
                                    joined(snapshot)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Bugfix", "Failed to read value.", error.toException())
                }
            })
    }

    private fun joined(snapshot: DataSnapshot){
        Log.d("Bugfix", "Host JOINED")
        try {
            val ohl = snapshot.getValue(OnlineHostLobby::class.java)!!

            val sp = PreferenceManager.getDefaultSharedPreferences(this.context)


            Game.getInstance().setSharedPreferences(sp)

            OnlineGame.getInstance().setLobby(ohl)
            OnlineGame.getInstance().setMyColor(PLAYER.BLUE)

            val intent = Intent(requireContext(), GameActivity::class.java)
            intent.putExtra("isOnline", true)
            startActivity(intent)
        } catch (e: Exception){
            Log.e("Bugfix", "HostGame joined: ${e.message}")
        }
        /*
            val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
            val onlineGameTransferObj = snapshot.child("ogto").getValue(OnlineGameTransferObj::class.java)!!

            database.getReference("gameRooms").child("${onlineHostLobby?.getConnString()}").child("ogto").child("blueName").setValue(onlineGameTransferObj.blueName)

            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", false)
            editor.apply()

            if(onlineGameTransferObj.blueName == onlineGameTransferObj.lastPlayer)
                OnlineGame.getInstance().setFirstPlayer(PLAYER.RED)
            else
                OnlineGame.getInstance().setFirstPlayer(PLAYER.BLUE)

            OnlineGame.getInstance().setOnlinePlayerName(onlineGameTransferObj.blueName)

            OnlineGame.getInstance().setOnlineGameTransferObj(onlineGameTransferObj)
            OnlineGame.getInstance().setOnlineHostLobby(onlineHostLobby!!)
            OnlineGame.getInstance().setSharedPreferences(sp)
            OnlineGame.getInstance().isOnline(true)
            OnlineGame.getInstance().startNewGame()
        } catch (e: Exception) {
            Log.e("Bugfix", "${e.message}")
        }
        val intent = Intent(requireContext(), GameActivity::class.java)
        intent.putExtra("isOnline", true)
        startActivity(intent)
         */

    }
}