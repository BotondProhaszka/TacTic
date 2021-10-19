package hu.bme.aut.tactic.fragments

 import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
 import hu.bme.aut.tactic.activities.GameActivity
import hu.bme.aut.tactic.databinding.MainMenuFragmentBinding

class MainMenuFragment : Fragment() {
    private lateinit var binding: MainMenuFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainMenuFragmentBinding.inflate(inflater, container, false)

        binding.btnStart.setOnClickListener{
            val intent = Intent(this.context, GameActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettings.setOnClickListener{
            SettingsFragment().show(this.parentFragmentManager, "SETTINGS")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}