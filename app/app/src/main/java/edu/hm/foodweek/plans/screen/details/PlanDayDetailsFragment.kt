package edu.hm.foodweek.plans.screen.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.android.synthetic.main.fragment_plan_details.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlanDayDetailsFragment : Fragment() {
    private val args: PlanDayDetailsFragmentArgs by navArgs()
    private val viewModel: PlanDetailsViewModel by viewModel { parametersOf(args.mealPlanId) }

    private lateinit var mAdapter: PlanDayDetailsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var timelineData = mutableListOf<PlanDayDetailsItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plan_details, container, false)
        viewModel.detailedItems(WeekDay.valueOf(args.dayId))
            .observe(this.viewLifecycleOwner, Observer { items ->
                timelineData.clear()
                timelineData.addAll(items)
                mAdapter.notifyDataSetChanged()
            })
        initRecyclerView(root.recyclerView, requireContext())
        return root
    }

    private fun initRecyclerView(recyclerView: RecyclerView, context: Context) {

        // Init recipe click listener
        val onRecipeClicked = { recipe: Recipe ->
            val action = PlanDayDetailsFragmentDirections.showRecipeDetail(recipe.recipeId)
            findNavController().navigate(action)
            Unit
        }

        // Init adapter
        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        mAdapter =
            PlanDayDetailsAdapter(
                requireContext(),
                timelineData,
                Glide.with(this).asDrawable(),
                onRecipeClicked
            )
        recyclerView.adapter = mAdapter
    }
}
