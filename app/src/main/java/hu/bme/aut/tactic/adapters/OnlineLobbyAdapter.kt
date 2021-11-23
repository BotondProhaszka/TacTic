package hu.bme.aut.tactic.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.tactic.fragments.HostGameFragment
import hu.bme.aut.tactic.fragments.JoinGameFragment

class OnlineLobbyAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val joinGameFragment = JoinGameFragment()
    private val hostGameFragment = HostGameFragment()

    companion object {
        private const val NUM_PAGES: Int = 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> joinGameFragment
            1 -> hostGameFragment
            else -> joinGameFragment
        }
    }

    override fun getItemCount(): Int = NUM_PAGES

    fun setDatabase(db: FirebaseDatabase){
        hostGameFragment.setDatabase(db)
        joinGameFragment.setDatabase(db)
    }
}