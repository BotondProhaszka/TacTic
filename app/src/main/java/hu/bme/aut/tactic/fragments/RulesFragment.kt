package hu.bme.aut.tactic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.tactic.databinding.RulesFragmentBinding

class RulesFragment : Fragment() {

    private lateinit var binding: RulesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RulesFragmentBinding.inflate(inflater)



        return binding.root
    }

    //TODO RulesFragment

}