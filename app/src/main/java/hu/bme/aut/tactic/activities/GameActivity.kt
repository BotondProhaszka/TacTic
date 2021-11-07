package hu.bme.aut.tactic.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityGameBinding
import hu.bme.aut.tactic.dialogs.NewOfflineGameDialog
import hu.bme.aut.tactic.dialogs.RestartGameDialog
import hu.bme.aut.tactic.model.Game
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private var game : Game = Game.getInstance()
    private lateinit var database: ScoresDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        thread {
            database = ScoresDatabase.getDatabase(this)
        }
        binding.tvBlueName.text = game.getBluePlayersName()
        binding.tvRedName.text = game.getRedPlayersName()

        initMapView()
        initPreferences()

        binding.btnDraw.setOnClickListener {
            gameOver(game.getScore())
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(R.string.r_u_sure_u_want_to_quit)
            .setPositiveButton(R.string.ok) { _, _ ->
                var menuIntent = Intent(this, MenuActivity::class.java)
                startActivity( menuIntent, null)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    fun gameOver(score: Score){
        thread {
            database.scoreDao().insert(score)
        }

        RestartGameDialog(this, score).show()
    }

    private fun initPreferences(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val x = sp.getInt("MAP_WIDTH_VAL", 5)
        val y = sp.getInt("MAP_HEIGHT_VAL", 5)
        val editor: SharedPreferences.Editor = sp.edit()
        var shouldShow = sp.getBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", true)

        game.startNewGame(x, y)
        game.setGameActivity(this)

        if(shouldShow) {
            editor.putBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", false)
            editor.apply()
            Log.d("Bugfix", "if: ${sp.getBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", true)}")
            NewOfflineGameDialog(this).show()
        }
    }

    private fun initMapView(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mapView: View = inflater.inflate(R.layout.map_view, null)
        val parent = binding.view
        parent.addView(mapView, parent.childCount -1)
    }
}