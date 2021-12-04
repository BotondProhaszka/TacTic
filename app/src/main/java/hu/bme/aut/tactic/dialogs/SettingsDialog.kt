package hu.bme.aut.tactic.dialogs

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.activities.MenuActivity
import hu.bme.aut.tactic.databinding.SettingsFragmentBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import hu.bme.aut.tactic.model.SP_MAP_SIZE_POS
import hu.bme.aut.tactic.model.SP_MAP_SIZE_VAL
import hu.bme.aut.tactic.model.SP_NIGHT_MODE
import hu.bme.aut.tactic.model.SP_PLAYER_NAME
import java.lang.Exception
import kotlin.collections.ArrayList
import kotlin.random.Random


class SettingsDialog: DialogFragment(), AdapterView.OnItemSelectedListener{
    private lateinit var binding: SettingsFragmentBinding
    private var spinValue = 5
    private val intArray = ArrayList<Int>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        initTheme()
        setThemeListener()
        initSpinners()
        initPlayerName()

        binding.btnApply.setOnClickListener {
            val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putInt(SP_MAP_SIZE_POS, binding.spinner.selectedItemPosition)
            editor.putInt(SP_MAP_SIZE_VAL, binding.spinner.selectedItem as Int)
            editor.putString(SP_PLAYER_NAME, binding.etPlayerName.text.toString())
            editor.apply()
            dialog?.dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p0?.id == R.id.spinner)
            spinValue = intArray[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) { }

    private fun initSpinners(){
        for(i in 4..20){
            intArray.add(i)
        }
        val aa = ArrayAdapter(this.requireContext(), R.layout.spinner_item, intArray)
        binding.spinner.adapter = aa
        binding.spinner.onItemSelectedListener = this

        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val pos = sp.getInt(SP_MAP_SIZE_POS, 0)

        binding.spinner.setSelection(pos)
    }

    private fun setThemeListener(){
        try {
            val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
            val editor: SharedPreferences.Editor = sp.edit()

            binding.ibNightmode.setOnClickListener {

                val isDark =
                    AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

                when (sp.getBoolean(SP_NIGHT_MODE, isDark)) {
                    true -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        binding.ibNightmode.setImageResource(R.drawable.ic_outline_brightness_low_24)
                        editor.putBoolean(SP_NIGHT_MODE, false)
                        editor.apply()
                    }
                    false -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        binding.ibNightmode.setImageResource(R.drawable.ic_twotone_brightness_3_24)
                        editor.putBoolean(SP_NIGHT_MODE, true)
                        editor.apply()
                    }
                }
                val intent = Intent(this.context, MenuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        } catch (e: Exception){
            Log.e("Bugfix", "Settings: ${e.message}")
        }
    }

    private fun initTheme(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)

        when (sp.getBoolean(SP_NIGHT_MODE, true)) {
            true -> {
                binding.ibNightmode.setImageResource(R.drawable.ic_outline_brightness_low_24)
            }
            false -> {
                binding.ibNightmode.setImageResource(R.drawable.ic_twotone_brightness_3_24)
            }
        }
    }


    private fun initPlayerName(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this.context)
        val rndNumb = Random.nextInt(1000000)
        binding.etPlayerName.setText(sp.getString(SP_PLAYER_NAME, "guestPlayer$rndNumb"))
    }
}