package edu.hm.foodweek.plans.screen.manage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentManagePlansBinding
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.plans.screen.PlanFragmentDirections
import org.koin.android.ext.android.inject

class ManagePlansFragment : Fragment() {

    private val mealPlanViewModel: MealPlanViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentManagePlansBinding>(
            inflater,
            R.layout.fragment_manage_plans,
            container,
            false
        )
        binding.viewModel = mealPlanViewModel

        val adapter = ManagePlansAdapter(
            onCardClicked = {
                findNavController().navigate(
                    PlanFragmentDirections.startPlanDetails(
                        it.planId
                    )
                )
            },
            onEditClicked = { Log.i("ManagePlansFragment", "Editing of meal plan ${it.planId}") },
            onDeleteClicked = {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        // Delete selected mealplan from database
                        Toast.makeText(
                            requireContext(),
                            "MealPlan ${it.title} will be deleted!",
                            Toast.LENGTH_LONG
                        ).show()
                        mealPlanViewModel.deletePlan(it)
                    }
                    .setNegativeButton("No") { _, _ ->
                        // Dismiss the dialog
                        Toast.makeText(
                            requireContext(),
                            "Deletion of MealPlan ${it.title} was canceled!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                val alert = builder.create()
                alert.show()
            },
            onUnsubscribeClicked = { mealPlanViewModel.unsubscribePlan(it) },
            onStatsClicked = { Log.i("ManagePlansFragment", "Stats of meal plan ${it.planId}") },
            onScheduleClicked = {
                Log.i(
                    "ManagePlansFragment",
                    "Scheduling of meal plan ${it.planId}"
                )
            }
        )
        val recyclerView = binding.managePlansRecyclerView
        recyclerView.adapter = adapter

        mealPlanViewModel.managedPlans.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        })
        return binding.root
    }
}
