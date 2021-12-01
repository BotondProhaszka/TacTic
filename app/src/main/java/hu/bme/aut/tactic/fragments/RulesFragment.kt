package hu.bme.aut.tactic.fragments

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.databinding.RulesFragmentBinding
import kotlin.properties.Delegates

class RulesFragment : Fragment() {

    private lateinit var binding: RulesFragmentBinding
    private var counter = 0
    private val rules = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RulesFragmentBinding.inflate(inflater)

        try {
            binding.ibNext.setOnClickListener { getNextRule() }
            binding.ibBack.setOnClickListener { getPrevRule() }
        } catch (e: Exception){
            Log.e("Bugfix", "${e.message}")
        }

        val res: Resources = requireActivity().resources
        val rulesTemp = res.getStringArray(R.array.rules)
        rules.clear()
        for(value in rulesTemp)
            rules.add(value.toString())
        binding.tvRule.text = rules[0]
        binding.ibBack.isEnabled = false
        initProgressBar()

        return binding.root
    }


    private fun getNextRule(){
        counter += 1
        updateProgressBar()
        if(counter + 1 == rules.size){
            binding.ibNext.isEnabled = false;
        } else if(counter == 1)
            binding.ibBack.isEnabled = true;
        binding.tvRule.text = rules[counter]
    }

    private fun getPrevRule(){
        counter -= 1
        updateProgressBar()
        if(counter == 0){
            binding.ibBack.isEnabled = false;
        } else if(counter == rules.size - 2)
            binding.ibNext.isEnabled = true
        binding.tvRule.text = rules[counter]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initProgressBar(){
        binding.progressBar.min = 0
        binding.progressBar.max = rules.size - 1
    }

    private fun updateProgressBar(){
        binding.progressBar.progress = counter
    }
}