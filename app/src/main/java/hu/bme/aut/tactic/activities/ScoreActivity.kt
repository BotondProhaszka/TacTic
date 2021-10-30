package hu.bme.aut.tactic.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.tactic.adapters.ScoresAdapter
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityScoreBinding
import hu.bme.aut.tactic.model.Game
import kotlin.concurrent.thread

class ScoreActivity : AppCompatActivity(), ScoresAdapter.ScoreClickListener {

    private lateinit var binding: ActivityScoreBinding

    private lateinit var database: ScoresDatabase
    private lateinit var adapter: ScoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = ScoresDatabase.getDatabase(applicationContext)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter = ScoresAdapter(this)
        binding.rvScores.layoutManager = LinearLayoutManager(this)
        binding.rvScores.adapter = adapter
        loadScoresInBackground()
    }

    private fun loadScoresInBackground(){
        thread{
            val scores = database.scoreDao().getAll().toMutableList()
            val scoresFromGame = Game.getInstance().getScores()

            if(scoresFromGame.isNotEmpty()) {
                for(s in scoresFromGame)
                    database.scoreDao().insert(s)
                scores.addAll(scoresFromGame)

            }
            runOnUiThread{
                adapter.update(scores)
            }
        }
    }

    override fun onScoreDeleted(score: Score) {
        thread {
            database.scoreDao().deleteItem(score)
            Log.d("ScoreActivity", "Item deleted")
        }
    }
}