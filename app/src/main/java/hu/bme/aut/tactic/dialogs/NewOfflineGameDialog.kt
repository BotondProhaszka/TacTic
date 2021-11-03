package hu.bme.aut.tactic.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.activities.MenuActivity
import hu.bme.aut.tactic.databinding.NewOfflineGameDialogBinding

class NewOfflineGameDialog(context: Context) : Dialog(context){
    private lateinit var binding: NewOfflineGameDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = NewOfflineGameDialogBinding.inflate(layoutInflater)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(binding.root)
        this.setCancelable(false)

        binding.btnStart.setOnClickListener {
            if (binding.etName1.text.isEmpty() || binding.etName2.text.isEmpty()) {
                Toast.makeText(this.context, "Did you forget to type your name?", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                val intent = Intent(this.context, GameActivity::class.java)
                this.context.startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {

        val menuIntent = Intent(context, MenuActivity::class.java)
        menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(this.context, menuIntent, null)
    }
}
