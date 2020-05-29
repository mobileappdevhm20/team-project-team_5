package edu.hm.foodweek.plans.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.hm.foodweek.R
import edu.hm.foodweek.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_plan_details.view.*


class PlanDetailsFragment : Fragment() {

    private val args: PlanDetailsFragmentArgs by navArgs()
    private val viewModel: PlanDetailsViewModel by viewModels {
        InjectorUtils.providePlanDetailsModelFactory(this, args.mealPlanId)
    }
    private lateinit var mAdapter: PlanTimelineAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    private var timelineData = mutableListOf<PlanTimelineItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plan_details, container, false)
        viewModel.items.observe(this.viewLifecycleOwner, Observer { items ->
            timelineData.clear()
            timelineData.addAll(items)
            mAdapter.notifyDataSetChanged()
        })
        initRecyclerView(root.recyclerView)
        return root
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        mLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = PlanTimelineAdapter(timelineData, Glide.with(this).asDrawable())
        recyclerView.adapter = mAdapter
    }
}
