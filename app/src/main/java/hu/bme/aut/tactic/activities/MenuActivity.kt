package hu.bme.aut.tactic.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.adapters.MenuPageAdapter
import hu.bme.aut.tactic.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMenuBinding
    private lateinit var  sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var themeId : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(500)

        initTheme()


        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        changeTheme(sharedPreferences.getInt("themeId", 0))

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpMenu.adapter = MenuPageAdapter(supportFragmentManager)



    }

    private fun changeTheme(id : Int){
        themeId = id
        setTheme(id)
        editor.putInt("themeId", id)
    }

    private fun initTheme(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        when (sp.getBoolean("NIGHT_MODE", true)) {
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}