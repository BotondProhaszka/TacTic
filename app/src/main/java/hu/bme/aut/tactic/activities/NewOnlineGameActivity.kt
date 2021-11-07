package hu.bme.aut.tactic.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.adapters.OnlineLobbyAdapter
import hu.bme.aut.tactic.databinding.ActivityNewOnlineGameBinding

class NewOnlineGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewOnlineGameBinding

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

        TabLayoutMediator(binding.tabLayout, binding.vpOnlineLobby) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.join)
                1 -> getString(R.string.host)
                else -> ""
            }
        }.attach()
    }
}