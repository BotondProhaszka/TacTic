package hu.bme.aut.tactic.fragments

 import android.content.Context
 import android.content.Intent
 import android.net.ConnectivityManager
 import android.net.NetworkCapabilities
 import android.os.Build
 import android.os.Bundle
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import androidx.fragment.app.Fragment
 import hu.bme.aut.tactic.activities.NewOnlineGameActivity
 import hu.bme.aut.tactic.databinding.MainMenuFragmentBinding
 import hu.bme.aut.tactic.dialogs.NewOfflineGameDialog
 import hu.bme.aut.tactic.dialogs.SettingsDialog
 import hu.bme.aut.tactic.model.Game
 import hu.bme.aut.tactic.model.GameHelper
 import hu.bme.aut.tactic.model.OnlineGame

class MainMenuFragment : Fragment() {
    private lateinit var binding: MainMenuFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainMenuFragmentBinding.inflate(inflater, container, false)

        binding.btnOffline.setOnClickListener {
            GameHelper.game = Game.getInstance()
            NewOfflineGameDialog(this.requireContext()).show()
        }

        binding.btnOnline.setOnClickListener{
            GameHelper.game = OnlineGame
            val intent = Intent(this.context, NewOnlineGameActivity::class.java)
            startActivity(intent)
        }


        binding.btnSettings.setOnClickListener{
            SettingsDialog().show(this.parentFragmentManager, "SETTINGS")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.btnOnline.isEnabled = isNetworkAvailable(this.context)
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