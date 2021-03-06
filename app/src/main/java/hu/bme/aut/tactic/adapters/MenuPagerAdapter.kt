package hu.bme.aut.tactic.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.bme.aut.tactic.data.ScoresDatabase
import hu.bme.aut.tactic.fragments.MainMenuFragment
import hu.bme.aut.tactic.fragments.RulesFragment
import hu.bme.aut.tactic.fragments.ScoreMenuFragment

class MenuPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val scoreMenuFragment = ScoreMenuFragment()

    companion object{
        const val NUM_PAGES = 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RulesFragment()
            1 -> MainMenuFragment()
            2 -> scoreMenuFragment
            else -> MainMenuFragment()
        }
    }

    override fun getItemCount() : Int = NUM_PAGES

    fun setDatabase(db: ScoresDatabase){
        scoreMenuFragment.setDatabase(db)
    }
}