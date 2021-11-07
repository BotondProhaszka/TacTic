package hu.bme.aut.tactic.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.tactic.databinding.HostGameFragmentBinding

class HostGameFragment: Fragment() {
    private lateinit var binding: HostGameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HostGameFragmentBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

}