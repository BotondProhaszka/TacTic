package hu.bme.aut.tactic.dialogs

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
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
import androidx.annotation.RequiresApi


class SettingsDialog: DialogFragment(), AdapterView.OnItemSelectedListener{
    private lateinit var binding: SettingsFragmentBinding
    private var spinXValue = 5
    private val intArray = ArrayList<Int>()
    private var spinYValue = 5


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        initTheme()
        setThemeListener()
        initSpinners()

        binding.btnApply.setOnClickListener{
            val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putInt("MAP_WIDTH_POS", binding.spinnerX.selectedItemPosition)
            editor.putInt("MAP_HEIGHT_POS", binding.spinnerY.selectedItemPosition)
            editor.putInt("MAP_WIDTH_VAL", binding.spinnerX.selectedItem as Int)
            editor.putInt("MAP_HEIGHT_VAL", binding.spinnerY.selectedItem as Int)
            editor.apply()
            dialog?.dismiss()
        }


        return binding.root
    }



    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p0?.id == R.id.spinnerX)
            spinXValue = intArray[p2]
        else if(p0?.id == R.id.spinnerY)
            spinYValue = intArray[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun initSpinners(){
        for(i in 4..9){
            intArray.add(i)
        }
        val aa = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, intArray)
        binding.spinnerX.adapter = aa
        binding.spinnerY.adapter = aa
        binding.spinnerX.onItemSelectedListener = this
        binding.spinnerY.onItemSelectedListener = this

        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val xPos = sp.getInt("MAP_WIDTH_POS", 0)
        val yPos = sp.getInt("MAP_HEIGHT_POS", 0)

        binding.spinnerX.setSelection(xPos)
        binding.spinnerY.setSelection(yPos)
    }

    private fun setThemeListener(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val editor: SharedPreferences.Editor = sp.edit()


        binding.ibNightmode.setOnClickListener {
            when (sp.getBoolean("NIGHT_MODE", false)) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.ibNightmode.setImageResource(R.drawable.ic_outline_brightness_low_24)
                    editor.putBoolean("NIGHT_MODE", false)
                    editor.apply()
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.ibNightmode.setImageResource(R.drawable.ic_twotone_brightness_3_24)
                    editor.putBoolean("NIGHT_MODE", true)
                    editor.apply()
                }
            }
        }
    }

    private fun initTheme(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        when (sp.getBoolean("NIGHT_MODE", true)) {
            true -> {
                binding.ibNightmode.setImageResource(R.drawable.ic_outline_brightness_low_24)
            }
            false -> {
                binding.ibNightmode.setImageResource(R.drawable.ic_twotone_brightness_3_24)
            }
        }
    }
}