package edu.hm.foodweek.plans.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentBrowsePlansBinding
import edu.hm.foodweek.util.InjectorUtils

/**
 * A simple [Fragment] subclass.
 */
class BrowsePlansFragment : Fragment() {

    private lateinit var mealPlanViewModel: MealPlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentBrowsePlansBinding>(
            inflater,
            R.layout.fragment_browse_plans,
            container,
            false
        )
        mealPlanViewModel = InjectorUtils.provideMealPlanViewModel(this)
        binding.viewModel = mealPlanViewModel

        // Navigate to details view on Card-Image click
        val onCardClicked = { planId: Long ->
            val action = PlanFragmentDirections.startPlanDetails(planId)
            findNavController().navigate(action)
        }

        val adapter = BrowsePlansAdapter(onCardClicked)
        val recyclerView = binding.plansList
        recyclerView.adapter = adapter

        mealPlanViewModel.allMealPlans.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })
        return binding.root
    }
}
