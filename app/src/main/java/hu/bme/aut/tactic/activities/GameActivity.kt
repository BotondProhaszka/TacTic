package hu.bme.aut.tactic.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.databinding.ActivityGameBinding
import hu.bme.aut.tactic.model.Game
import hu.bme.aut.tactic.model.PLAYER


class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding

    private var game : Game = Game.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val x = sp.getInt("MAP_WIDTH_VAL", 1)
        val y = sp.getInt("MAP_HEIGHT_VAL", 1)

        game.startNewGame(x, y)
        game.setRandomFirstPlayer()
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mapView: View = inflater.inflate(R.layout.map_view, null)
        val parent = binding.view
        parent!!.addView(mapView, parent!!.childCount -1)
    }
}