package hu.bme.aut.tactic.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.adapters.OnlineLobbyAdapter
import hu.bme.aut.tactic.databinding.ActivityNewOnlineGameBinding
import kotlin.concurrent.thread

class NewOnlineGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewOnlineGameBinding
    private val database = FirebaseDatabase.getInstance("https://tactic-add7c-default-rtdb.europe-west1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewOnlineGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

    }

    override fun onResume() {
        super.onResume()
        val onlineLobbyAdapter = OnlineLobbyAdapter(this)
        binding.vpOnlineLobby.adapter = onlineLobbyAdapter
        onlineLobbyAdapter.setDatabase(database)

        TabLayoutMediator(binding.tabLayout, binding.vpOnlineLobby) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.join)
                1 -> getString(R.string.host)
                else -> ""
            }
        }.attach()
    }
}