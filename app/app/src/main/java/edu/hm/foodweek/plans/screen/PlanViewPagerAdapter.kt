package edu.hm.foodweek.plans.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import edu.hm.foodweek.plans.screen.browse.BrowsePlansFragment
import edu.hm.foodweek.plans.screen.manage.ManagePlansFragment

class PlanViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                BrowsePlansFragment()
            }
            else -> {
                ManagePlansFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> {
               "Browse"
            }
            else -> {
               "Manage"
            }
        }
    }
}