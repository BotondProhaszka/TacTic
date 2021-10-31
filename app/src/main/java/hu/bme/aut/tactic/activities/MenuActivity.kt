package hu.bme.aut.tactic.activities

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.adapters.MenuPageAdapter
import hu.bme.aut.tactic.adapters.ScoresAdapter
import hu.bme.aut.tactic.data.Score
import hu.bme.aut.tactic.data.ScoreDao
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityMenuBinding
import hu.bme.aut.tactic.model.Game

class MenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMenuBinding
    private lateinit var  sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var adapter: ScoresAdapter

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

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(R.string.r_u_sure_u_want_to_quit)
            .setPositiveButton(R.string.ok) { _, _ -> finish()}
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}