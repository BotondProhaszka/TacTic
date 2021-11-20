package hu.bme.aut.tactic.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.databinding.ScoreRowBinding
import hu.bme.aut.tactic.model.PLAYER

class ScoresAdapter (private val listener: ScoreClickListener) :
    RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>(){

    private val items = mutableListOf<Score>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  ScoreViewHolder(ScoreRowBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = items[position]

        holder.binding.tvPlayer1Name.text = score.player1Name
        holder.binding.tvPlayer1Score.text = score.player1Score.toString()
        holder.binding.tvPlayer2Name.text = score.player2Name
        holder.binding.tvPlayer2Score.text = score.player2Score.toString()

        if(score.winner != null) {
            val color = when (score.winner) {
                PLAYER.BLUE -> Color.BLUE
                else -> Color.RED
            }
            holder.binding.tvPlayer1Name.setTextColor(color)
            holder.binding.tvPlayer1Score.setTextColor(color)
            holder.binding.tvPlayer2Name.setTextColor(color)
            holder.binding.tvPlayer2Score.setTextColor(color)
        }


        holder.binding.ibDeleteRow.setOnClickListener{
            remove(score)
        }
    }

   fun addScore(score: Score){
       items.add(score)
       notifyItemChanged(items.size-1)
   }

    fun addScores(scores: List<Score>){
        items.addAll(scores)
        notifyDataSetChanged()
    }

    fun update(scores: List<Score>){
        items.clear()
        items.addAll(scores)
        notifyDataSetChanged()
    }

    private fun remove(score: Score){
        items.remove(score)
        listener.onScoreDeleted(score)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface ScoreClickListener{
        fun onScoreDeleted(score: Score)
    }

    inner class ScoreViewHolder(val binding: ScoreRowBinding) : RecyclerView.ViewHolder(binding.root)
}
