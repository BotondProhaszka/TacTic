package hu.bme.aut.tactic.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.activities.MenuActivity
import hu.bme.aut.tactic.databinding.SettingsFragmentBinding

import android.widget.AdapterView
import android.widget.ArrayAdapter


class SettingsFragment: DialogFragment() {
    private lateinit var binding: SettingsFragmentBinding
    private var spinXValue = 4
    private val wArray = ArrayList<Int>()
    private var spinYValue = 4
    private val hArray = ArrayList<Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        when (this.context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                binding.ivNightMode.setImageResource(R.drawable.ic_nightmode)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.ivNightMode.setImageResource(R.drawable.ic_daymode)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.ivNightMode.setImageResource(R.drawable.ic_nightmode)
            }
        }

        binding.ivNightMode.setOnClickListener {
            when (this.context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.ivNightMode.setImageResource(R.drawable.ic_nightmode)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.ivNightMode.setImageResource(R.drawable.ic_daymode)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.ivNightMode.setImageResource(R.drawable.ic_nightmode)
                }
            }

        }
        binding.spinnerX.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { spinXValue = wArray[p2] }
        }
        binding.spinnerY.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { spinYValue = hArray[p2] }
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}