package hu.bme.aut.tactic.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.databinding.ActivityGameBinding
import hu.bme.aut.tactic.model.Game


class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding

    private var game : Game = Game.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        game.setMapSize(9, 9)
        Log.d("Bugfix", "Game.setMapSize()")
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mapView: View = inflater.inflate(R.layout.map_fragment, null)
        val parent = binding.view
        parent!!.addView(mapView, parent!!.childCount -1)
    }
}