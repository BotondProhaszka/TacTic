package hu.bme.aut.tactic.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.tactic.fragments.MainMenuFragment
import hu.bme.aut.tactic.fragments.ScoreMenuFragment

class MenuPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when(position){
        0 -> MainMenuFragment()
        1 -> ScoreMenuFragment()
        else -> MainMenuFragment()
    }

    override fun getCount() : Int = NUM_PAGES

    companion object{
        const val NUM_PAGES = 2
    }
}