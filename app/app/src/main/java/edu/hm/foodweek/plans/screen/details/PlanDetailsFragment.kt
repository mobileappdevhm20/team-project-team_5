package edu.hm.foodweek.plans.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentPlanDetailsBinding
import edu.hm.foodweek.plans.persistence.model.WeekDay
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlanDetailsFragment : Fragment() {

    private val args: PlanDetailsFragmentArgs by navArgs()
    private val viewModel: PlanDetailsViewModel by viewModel { parametersOf(args.mealPlanId) }

    private lateinit var mAdapter: PlanDetailsAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var timelineData = mutableListOf<PlanDetailsItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlanDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_details, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = viewModel
        viewModel.planImage.observe(viewLifecycleOwner, Observer {
            Glide
                .with(binding.planPreviewImage)
                .asDrawable()
                .load(it)
                .placeholder(R.drawable.no_image)
                .centerCrop()
                .priority(Priority.HIGH)
                .into(binding.planPreviewImage)
        })
        viewModel.items.observe(this.viewLifecycleOwner, Observer { items ->
            timelineData.clear()
            timelineData.addAll(items)
            mAdapter.notifyDataSetChanged()
        })

        // Navigate to day details view on Day-Image click
        val onDayClicked = { day: WeekDay ->
            val action =
                PlanDetailsFragmentDirections.startPlanDayDetails(
                    args.mealPlanId,
                    day.toString()
                )
            findNavController().navigate(action)
        }

        // Init recycler view
        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.planDetailsRecyclerView.layoutManager = mLayoutManager
        mAdapter = PlanDetailsAdapter(
            timelineData,
            Glide.with(this).asDrawable(),
            onDayClicked
        )
        binding.planDetailsRecyclerView.adapter = mAdapter

        return binding.root
    }

}
