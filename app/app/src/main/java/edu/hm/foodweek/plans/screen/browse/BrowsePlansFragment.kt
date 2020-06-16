package edu.hm.foodweek.plans.screen.browse

import android.os.Bundle
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
import edu.hm.foodweek.util.extensions.combineLatest
import edu.hm.foodweek.util.extensions.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class BrowsePlansFragment : Fragment(), KoinComponent {

    private val mealPlanViewModel: MealPlanViewModel by inject()
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

        // Subscribe or unsubscribe plan
        val onSubscribeClicked = { plan: BrowsableMealPlan ->
            if (plan.subscribed) {
                mealPlanViewModel.unsubscribePlan(plan.plan)
            } else {
                mealPlanViewModel.subscribePlan(plan.plan)
            }
        }

        val adapter = BrowsePlansAdapter(onCardClicked, onSubscribeClicked)
        val recyclerView = binding.plansList
        recyclerView.adapter = adapter

        mealPlanViewModel.browsablePlans
            .observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.data = it.toMutableList()
                    adapter.notifyDataSetChanged()
                }
            })

        recyclerView.addOnScrollListener(object :
            EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadPage(page: Int) {
                if (!loading) {
                    loading = true
                    mealPlanRepository.getLiveDataAllMealPlans(
                        query = binding.browsePlansSearchview.query.toString(),
                        page = page
                    )
                        .combineLatest(mealPlanViewModel.managedPlans)
                        .map { combined ->
                            combined?.first?.map { plan ->
                                BrowsableMealPlan(
                                    plan = plan,
                                    subscribed = combined.second.any { it.plan.planId == plan.planId }
                                )
                            }
                        }
                        .observe(viewLifecycleOwner, Observer { nextPage ->
                            val start = adapter.data.size - 1
                            adapter.data.addAll(nextPage!!)
                            adapter.notifyItemRangeChanged(start, nextPage.size)
                            loading = false
                        })
                }
            }
        })

        return binding.root
    }
}
