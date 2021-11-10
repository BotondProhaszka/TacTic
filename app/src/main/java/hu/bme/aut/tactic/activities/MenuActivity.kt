package hu.bme.aut.tactic.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.adapters.MenuPagerAdapter
import hu.bme.aut.tactic.adapters.ScoresAdapter
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityMenuBinding
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMenuBinding
    private lateinit var  sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var adapter: ScoresAdapter
    private lateinit var database: ScoresDatabase


    private var themeId : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(100)

        initTheme()

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        changeTheme(sharedPreferences.getInt("themeId", 0))

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vpadapter = MenuPagerAdapter(this)
        binding.vpMenu.adapter = vpadapter
        database = ScoresDatabase.getDatabase(this)
        vpadapter.setDatabase(database)
        thread {
            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("SHOULD_SHOW_NEW_GAME_DIALOG", true)
            editor.apply()
        }
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

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(R.string.r_u_sure_u_want_to_quit)
            .setPositiveButton(R.string.ok) { _, _ ->
                run {

                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}