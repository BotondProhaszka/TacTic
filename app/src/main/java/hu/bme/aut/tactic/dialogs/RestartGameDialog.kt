package hu.bme.aut.tactic.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.activities.MenuActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.databinding.RestartGameDialogBinding
import hu.bme.aut.tactic.model.Game

class RestartGameDialog(score: Score): AppCompatDialogFragment() {

    private lateinit var binding: RestartGameDialogBinding
    private var score = score
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restart_game_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RestartGameDialogBinding.inflate(layoutInflater)
        initTextViews()

        Log.d("Bugfix", "ide azért elérek")
        binding.btnRestart.setOnClickListener{
            Log.d("Bugfix", "btnRestart clicked")
            Game.getInstance().restartGame()
            dismiss()
        }

        binding.btnMenu.setOnClickListener{
            Log.d("Bugfix", "btnMenu clicked")
            val intent = Intent(this.context, MenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun initTextViews(){
        if (score.player1Score >= score.player2Score){
            binding.tvWinnerName.text = score.player1Name
            binding.tvWinnerScore.text = score.player1Score.toString()
            binding.tvLooserName.text = score.player2Name
            binding.tvLooserName.text = score.player2Score.toString()
        }
        else {

            binding.tvWinnerName.text = score.player2Name
            binding.tvWinnerScore.text = score.player2Score.toString()
            binding.tvLooserName.text = score.player1Name
            binding.tvLooserName.text = score.player1Score.toString()

        }
    }
}