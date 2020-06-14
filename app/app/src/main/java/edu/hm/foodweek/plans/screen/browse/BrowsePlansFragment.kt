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
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.plans.screen.PlanFragmentDirections
import edu.hm.foodweek.plans.screen.EndlessScrollListener
import edu.hm.foodweek.util.amplify.FoodWeekClient
import edu.hm.foodweek.util.amplify.MealPlanResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * A simple [Fragment] subclass.
 */
class BrowsePlansFragment : Fragment(), KoinComponent {

    private val mealPlanViewModel: MealPlanViewModel by viewModel()
    private val foodWeekClient: FoodWeekClient by inject()

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
                // inline this line, if you want to fire a call on each searchbar change
                // mealPlanViewModel.filterText.postValue(newText)
                if (newText.isNullOrEmpty()) {
                    mealPlanViewModel.filterText.postValue("")
                }
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
                adapter.data = it
            }
        })

        recyclerView.addOnScrollListener(object :
            EndlessScrollListener(LinearLayoutManager(this.context)) {
            override fun loadMoreItems() {
                loading = true
                currentPage += 1
                Log.println(
                    Log.INFO,
                    "BrowsePlansFragment",
                    "isLoading? " + isLoading() + " currentPage " + currentPage
                )
                // Do API Call and update livedata
                foodWeekClient.getFoodWeekServiceClient()
                    .getMealPlans(currentPage, 20, binding.browsePlansSearchview.query.toString())
                    .enqueue(object : Callback,
                        retrofit2.Callback<MealPlanResponse> {
                        override fun onFailure(call: Call<MealPlanResponse>, t: Throwable) {
                            Log.println(
                                Log.ERROR,
                                "BrowsePlansFragment",
                                "HTTP-Request /mealplans failed: ${t.message}"
                            )
                        }

                        override fun onResponse(
                            call: Call<MealPlanResponse>,
                            response: Response<MealPlanResponse>
                        ) {
                            Log.println(
                                Log.INFO,
                                "BrowsePlansFragment",
                                "HTTP-Request /mealplans for additional mealplans: ${response.code()}"
                            )
                            val mealPlanResponse = response.body()
                            lastPage = mealPlanResponse!!.lastPage
                            val foundMealPlans = response.body()?.mealPlans ?: emptyList()
                            val currentMealPlans = ArrayList<MealPlan>()
                            currentMealPlans.addAll(adapter.data)
                            currentMealPlans.addAll(foundMealPlans)
                            adapter.data = currentMealPlans
                            adapter.notifyDataSetChanged()
                        }
                    })
                loading = false
            }

            override fun isLastPage(): Boolean {
                return lastPage
            }

            override fun isLoading(): Boolean {
                return loading
            }
        })

        return binding.root
    }
}
