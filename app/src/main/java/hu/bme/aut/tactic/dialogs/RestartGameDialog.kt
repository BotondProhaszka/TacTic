package hu.bme.aut.tactic.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import hu.bme.aut.tactic.activities.MenuActivity
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.databinding.RestartGameDialogBinding
import hu.bme.aut.tactic.model.Game
import hu.bme.aut.tactic.model.GameHelper

class RestartGameDialog(context: Context, private var score: Score): Dialog(context) {

    private lateinit var binding: RestartGameDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RestartGameDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTextViews()

        if(GameHelper.game.isOnline()){
            binding.btnRestart.visibility = View.GONE
        }

        binding.btnRestart.setOnClickListener{
            Game.restartGame()
            dismiss()
        }

        binding.btnMenu.setOnClickListener {

            val intent = Intent(this.context, MenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(context, intent, null)

        }
    }

    override fun onStart() {
        super.onStart()
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialog: Dialog = this
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window?.setLayout(width, height)
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