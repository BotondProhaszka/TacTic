package hu.bme.aut.tactic.activities

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.tactic.R
import hu.bme.aut.tactic.adapters.MenuPagerAdapter
import hu.bme.aut.tactic.adapters.ScoresAdapter
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.databinding.ActivityMenuBinding
import hu.bme.aut.tactic.model.SP_NAME
import hu.bme.aut.tactic.model.SP_NIGHT_MODE

class MenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMenuBinding
    private lateinit var  sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var adapter: ScoresAdapter
    private lateinit var database: ScoresDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTheme()

        sharedPreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        binding = ActivityMenuBinding.inflate(layoutInflater)
        val vpAdapter = MenuPagerAdapter(this)
        binding.vpMenu.adapter = vpAdapter
        database = ScoresDatabase.getDatabase(this)
        vpAdapter.setDatabase(database)

        binding.vpMenu.setCurrentItem(1, false)

        TabLayoutMediator(binding.tabLayout, binding.vpMenu) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.rules)
                1 -> getString(R.string.home)
                2 -> getString(R.string.scores)
                else -> ""
            }
        }.attach()

        Thread.sleep(400)
        setContentView(binding.root)
    }


    private fun initTheme(){
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        when (sp.getBoolean(SP_NIGHT_MODE, true)) {
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


    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}