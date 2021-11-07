package hu.bme.aut.tactic.fragments

 import android.content.Intent
import android.os.Bundle
 import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
 import hu.bme.aut.tactic.activities.GameActivity
 import hu.bme.aut.tactic.activities.NewOnlineGameActivity
 import hu.bme.aut.tactic.activities.ScoreActivity
 import hu.bme.aut.tactic.databinding.MainMenuFragmentBinding
 import hu.bme.aut.tactic.dialogs.SettingsDialog

class MainMenuFragment : Fragment() {
    private lateinit var binding: MainMenuFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainMenuFragmentBinding.inflate(inflater, container, false)

        binding.btnOffline.setOnClickListener {
            val intent = Intent(this.context, GameActivity::class.java)
            startActivity(intent)
        }

        binding.btnOnline.setOnClickListener{
            val intent = Intent(this.context, NewOnlineGameActivity::class.java)
            startActivity(intent)
        }

        binding.btnScores.setOnClickListener{
            val intent = Intent(this.context, ScoreActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettings.setOnClickListener{
            SettingsDialog().show(this.parentFragmentManager, "SETTINGS")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}