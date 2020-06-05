package edu.hm.foodweek.plans.screen.details

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.github.vipulasri.timelineview.TimelineView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.android.synthetic.main.plan_day_details_view_holder.view.*
import kotlinx.android.synthetic.main.plan_details_view_holder.view.timeline

class PlanDayDetailsAdapter(
    private val context: Context,
    private val mFeedList: List<PlanDayDetailsItem>,
    private val request: RequestBuilder<Drawable>,
    private val onRecipeClicked: (Recipe) -> Unit
) : RecyclerView.Adapter<PlanDayDetailsAdapter.PlanDayDetailsTimelineViewHolder>() {
    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanDayDetailsTimelineViewHolder {
        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return PlanDayDetailsTimelineViewHolder(
            mLayoutInflater.inflate(
                R.layout.plan_day_details_view_holder,
                parent,
                false
            ), viewType
        )
    }

    override fun onBindViewHolder(holder: PlanDayDetailsTimelineViewHolder, position: Int) {
        val day = mFeedList[position]
        holder.mealTime.text = day.time.name

        // Init recipe list
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.recipeList.layoutManager = mLayoutManager
        val mAdapter =
            BriefRecipeAdapter(
                day.recipes,
                request,
                onRecipeClicked
            )
        holder.recipeList.adapter = mAdapter

    }

    override fun getItemCount() = mFeedList.size

    inner class PlanDayDetailsTimelineViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {

        val mealTime: TextView = itemView.meal_time_label
        val recipeList: RecyclerView = itemView.recyclerView
        private val timeline: TimelineView = itemView.timeline

        init {
            timeline.initLine(viewType)
        }
    }

}