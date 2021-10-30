package hu.bme.aut.tactic.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityGameBinding
import hu.bme.aut.tactic.model.Game


class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding

    private var game : Game = Game.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val x = sp.getInt("MAP_WIDTH_VAL", 5)
        val y = sp.getInt("MAP_HEIGHT_VAL", 5)

        val db = ScoresDatabase.getDatabase(this)
        game.setDB(db)
        game.startNewGame(x, y)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mapView: View = inflater.inflate(R.layout.map_view, null)
        val parent = binding.view
        parent.addView(mapView, parent.childCount -1)
    }
}