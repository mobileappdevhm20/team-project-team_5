package edu.hm.foodweek.plans.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentPlanBinding

class PlanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPlanBinding>(
            inflater,
            R.layout.fragment_plan,
            container,
            false
        )
        val tabLayout = binding.planTabLayout
        val planViewPagerAdapter = PlanViewPagerAdapter(childFragmentManager)
        val planViewPager = binding.planViewPager
        planViewPager.adapter = planViewPagerAdapter
        tabLayout.setupWithViewPager(planViewPager)
        return binding.root
    }
}
