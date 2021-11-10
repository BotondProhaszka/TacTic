package hu.bme.aut.tactic.fragments

import android.os.Bundle
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
    private lateinit var adapter: ScoresAdapter
    private lateinit var db: ScoresDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScoreMenuFragmentBinding.inflate(inflater)

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView(){
        adapter = ScoresAdapter(this)
        binding.rvScores.layoutManager = LinearLayoutManager(this.context)
        binding.rvScores.adapter = adapter
        loadScoresInBackground()
    }

    private fun loadScoresInBackground(){
        thread{
            val scores = db.scoreDao().getAll().toMutableList()
            activity?.runOnUiThread{
                adapter.update(scores)
            }
        }
    }

    override fun onScoreDeleted(score: Score) {
        thread {
            db.scoreDao().deleteItem(score)
        }
    }

    fun setDatabase(db: ScoresDatabase){
        this.db = db
    }
}