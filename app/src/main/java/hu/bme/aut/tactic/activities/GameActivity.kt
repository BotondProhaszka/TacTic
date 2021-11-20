package hu.bme.aut.tactic.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.tactic.interfaces.GameInterface
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityGameBinding
import hu.bme.aut.tactic.dialogs.RestartGameDialog
import hu.bme.aut.tactic.model.Game
import hu.bme.aut.tactic.model.MapViewHelper
import hu.bme.aut.tactic.model.OnlineGame
import java.lang.Exception
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private lateinit var game : GameInterface
    private lateinit var database: ScoresDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            binding = ActivityGameBinding.inflate(layoutInflater)
            setContentView(binding.root)

            game = MapViewHelper.game

            if (intent.extras?.get("isOnline") == false) {
                Log.d("Bugfix", "OfflineGame started")
            } else {
                Log.d("Bugfix", "OnlineGame started")
            }

            thread {
                database = ScoresDatabase.getDatabase(this)
            }
            binding.tvBlueName.text = game.getBluePlayersName()
            binding.tvRedName.text = game.getRedPlayersName()


            game.setGameActivity(this)
            game.startNewGame(game.getFirstPlayer())

            initMapView()


            binding.btnDraw.setOnClickListener {
                gameOver(game.getScore())
            }
        } catch (e: Exception){
            Log.e("Bugfix", "Error: ${e.message}")
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(R.string.r_u_sure_u_want_to_quit)
            .setPositiveButton(R.string.ok) { _, _ ->
                val menuIntent = Intent(this, MenuActivity::class.java)
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
        game.closeGameRoom()
    }

    @SuppressLint("InflateParams")
    private fun initMapView(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mapView: View = inflater.inflate(R.layout.map_view, null)
        val parent = binding.view
        parent.addView(mapView, parent.childCount -1)
    }


    fun getContext(): Context = this


}