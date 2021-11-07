package hu.bme.aut.tactic.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.bme.aut.tactic.fragments.HostGameFragment
import hu.bme.aut.tactic.fragments.JoinGameFragment

class OnlineLobbyAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val NUM_PAGES: Int = 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> JoinGameFragment()
            1 -> HostGameFragment()
            else -> JoinGameFragment()
        }
    }

    override fun getItemCount(): Int = NUM_PAGES

}