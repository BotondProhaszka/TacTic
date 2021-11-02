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
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.activities.MenuActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.databinding.RestartGameDialogBinding
import hu.bme.aut.tactic.model.Game

class RestartGameDialog(context: Context, score: Score): Dialog(context) {

    private lateinit var binding: RestartGameDialogBinding
    private var score = score

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RestartGameDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTextViews()
        binding.btnRestart.setOnClickListener{
            Log.d("Bugfix", "btnRestart clicked")
            Game.getInstance().restartGame()
            dismiss()
        }

        binding.btnMenu.setOnClickListener{
            Log.d("Bugfix", "btnMenu clicked")
            val intent = Intent(this.context, MenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(context, intent, null)
        }
    }

    private fun initTextViews(){
        if (score.player1Score >= score.player2Score){
            binding.tvWinnerName.text = score.player1Name
            binding.tvWinnerScore.text = score.player1Score.toString()
            binding.tvLooserName.text = score.player2Name
            binding.tvLooserScore.text = score.player2Score.toString()
        }
        else {

            binding.tvWinnerName.text = score.player2Name
            binding.tvWinnerScore.text = score.player2Score.toString()
            binding.tvLooserName.text = score.player1Name
            binding.tvLooserScore.text = score.player1Score.toString()

        }
    }
}