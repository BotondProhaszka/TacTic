package hu.bme.aut.tactic.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import hu.bme.aut.tactic.databinding.NewOfflineGameDialogBinding
import hu.bme.aut.tactic.model.Game

class NewOfflineGameDialog(context: Context, game: Game) : Dialog(context){
    private val game = game
    private lateinit var binding: NewOfflineGameDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

}