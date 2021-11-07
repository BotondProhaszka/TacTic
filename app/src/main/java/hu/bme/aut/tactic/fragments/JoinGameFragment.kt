package hu.bme.aut.tactic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.tactic.databinding.JoinGameFragmentBinding

class JoinGameFragment: Fragment() {
    private lateinit var binding: JoinGameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = JoinGameFragmentBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }
}