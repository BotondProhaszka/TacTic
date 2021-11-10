package hu.bme.aut.tactic.fragments

import android.content.Intent
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
import hu.bme.aut.tactic.model.OnlineGameTransferObj
import hu.bme.aut.tactic.model.OnlineHostLobby
import hu.bme.aut.tactic.model.PLAYER
import kotlin.concurrent.thread

class HostGameFragment: Fragment() {
    private lateinit var binding: HostGameFragmentBinding
    private lateinit var database: FirebaseDatabase
    private var onlineHostLobby : OnlineHostLobby? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HostGameFragmentBinding.inflate(LayoutInflater.from(context))

        binding.btnHost.setOnClickListener { host() }
        binding.btnCancel.setOnClickListener { cancel() }

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }


    fun setDatabase(db: FirebaseDatabase){
        database = db
    }

    private fun host(){
        if(binding.etHostGameName.text.isEmpty())
            return

        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val width = sp.getInt("MAP_WIDTH_VAL", 5)
        val height = sp.getInt("MAP_HEIGHT_VAL", 5)
        onlineHostLobby = OnlineHostLobby(binding.etHostGameName.text.toString(), width, height)
        database.getReference("hostRooms").child("${onlineHostLobby?.lobbyName}").setValue(onlineHostLobby)
        database.getReference("hostGames").child("${onlineHostLobby?.getConnString()}").setValue(false)
        database.getReference("hostGames").child("${onlineHostLobby?.getConnString()}").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var data = snapshot.getValue(Boolean::class.java)
                if(data == true) {
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

    private fun cancel(){
        if(onlineHostLobby != null) {
            database.getReference("hostRooms").child("${onlineHostLobby?.lobbyName}").removeValue()
            binding.btnHost.isEnabled = true
            binding.btnCancel.isEnabled = false
            binding.etHostGameName.isEnabled = true
        }
    }

}