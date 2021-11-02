package hu.bme.aut.tactic.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.tactic.adapters.ScoresAdapter
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ScoreMenuFragmentBinding
import kotlin.concurrent.thread

class ScoreMenuFragment :Fragment(), ScoresAdapter.ScoreClickListener {
    private lateinit var binding: ScoreMenuFragmentBinding
    private lateinit var database: ScoresDatabase
    private lateinit var adapter: ScoresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScoreMenuFragmentBinding.inflate(inflater)

        if (container != null)
            database = ScoresDatabase.getDatabase(container.context)

        initRecyclerView()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initRecyclerView() {
        adapter = ScoresAdapter(this)
        binding.rvScores.layoutManager = LinearLayoutManager(this.context)
        binding.rvScores.adapter = adapter
        binding.rvScores.layoutManager = LinearLayoutManager(activity)
        loadScoresInBackground()
    }

    private fun loadScoresInBackground() {

        thread {
            var scores = database.scoreDao().getAll().toMutableList()
            Log.d("Bugfix", "${scores.size}")
            adapter.update(scores)
        }
    }

    override fun onScoreDeleted(score: Score) {
        thread {
            database.scoreDao().deleteItem(score)
            Log.d("ScoreActivity", "Item deleted")
        }
    }
}