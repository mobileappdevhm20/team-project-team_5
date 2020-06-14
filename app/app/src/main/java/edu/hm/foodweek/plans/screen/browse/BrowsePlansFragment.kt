package edu.hm.foodweek.plans.screen.browse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentBrowsePlansBinding
import edu.hm.foodweek.plans.persistence.MealPlanRepository
import edu.hm.foodweek.plans.screen.EndlessScrollListener
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.plans.screen.PlanFragmentDirections
import edu.hm.foodweek.util.amplify.FoodWeekClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class BrowsePlansFragment : Fragment(), KoinComponent {

    private val mealPlanViewModel: MealPlanViewModel by viewModel()
    private val foodWeekClient: FoodWeekClient by inject()
    private val mealPlanRepository: MealPlanRepository by inject()

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
        binding.viewModel = mealPlanViewModel

        binding.browsePlansSearchview.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                mealPlanViewModel.filterText.postValue(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                mealPlanViewModel.filterText.postValue(query)
                return true
            }
        })

        // Navigate to details view on Card-Image click
        val onCardClicked = { planId: Long ->
            val action =
                PlanFragmentDirections.startPlanDetails(
                    planId
                )
            findNavController().navigate(action)
        }

        val adapter =
            BrowsePlansAdapter(onCardClicked)
        val recyclerView = binding.plansList
        recyclerView.adapter = adapter

        mealPlanViewModel.filteredMealPlans.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it.toMutableList()
            }
        })

        recyclerView.addOnScrollListener(object :
            EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadPage(page: Int) {
                if (!loading) {
                    loading = true
                    Log.println(
                        Log.INFO,
                        "BrowsePlansFragment",
                        "isLoading? $loading currentPage $page"
                    )
                    mealPlanViewModel.mealPlanRepository.getLiveDataAllMealPlans(
                        query = binding.browsePlansSearchview.query.toString(),
                        page = page
                    ).observe(viewLifecycleOwner, Observer { nextPage ->
                        adapter.data.addAll(nextPage)
                        adapter.notifyDataSetChanged()
                        loading = false
                    })
                }
            }
        })

        return binding.root
    }
}
