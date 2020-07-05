package edu.hm.foodweek.plans.screen.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.plans.screen.browse.BrowseableMealPlan
import kotlinx.android.synthetic.main.browse_plans_view_holder.view.recipe_plan_picture
import kotlinx.android.synthetic.main.browse_plans_view_holder.view.recipe_plan_text
import kotlinx.android.synthetic.main.manage_plans_view_holder.view.*

class ManagePlansAdapter(
    private val onCardClicked: (MealPlan) -> Unit,
    private val onEditClicked: (MealPlan) -> Unit,
    private val onDeleteClicked: (MealPlan) -> Unit,
    private val onUnsubscribeClicked: (MealPlan) -> Unit,
    private val onScheduleClicked: (MealPlan) -> Unit,
    private val onStatsClicked: (MealPlan) -> Unit
) : RecyclerView.Adapter<ManagePlansAdapter.PlansViewHolder>() {

    var data = listOf<BrowseableMealPlan>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PlansViewHolder(
        itemView: View,
        private val onCardClicked: (MealPlan) -> Unit,
        private val onEditClicked: (MealPlan) -> Unit,
        private val onDeleteClicked: (MealPlan) -> Unit,
        private val onUnsubscribeClicked: (MealPlan) -> Unit,
        private val onScheduleClicked: (MealPlan) -> Unit,
        private val onStatsClicked: (MealPlan) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(mealPlan: BrowseableMealPlan) {

            // Set text
            itemView.recipe_plan_text.text = mealPlan.plan.title

            // Set visibility buttons
            if (mealPlan.owned) {
                itemView.button_delete_plan.visibility = View.VISIBLE
                itemView.button_edit_plan.visibility = View.VISIBLE
                itemView.button_plan_stats.visibility = View.VISIBLE
                itemView.button_unsubscribe.visibility = View.GONE
            } else {
                itemView.button_delete_plan.visibility = View.GONE
                itemView.button_edit_plan.visibility = View.GONE
                itemView.button_plan_stats.visibility = View.GONE
                itemView.button_unsubscribe.visibility = View.VISIBLE
            }

            // Add click listeners
            itemView.recipe_plan_picture.setOnClickListener { onCardClicked(mealPlan.plan) }
            itemView.button_edit_plan.setOnClickListener { onEditClicked(mealPlan.plan) }
            itemView.button_delete_plan.setOnClickListener { onDeleteClicked(mealPlan.plan) }
            itemView.button_unsubscribe.setOnClickListener { onUnsubscribeClicked(mealPlan.plan) }
            itemView.button_schedule_plan.setOnClickListener { onScheduleClicked(mealPlan.plan) }
            itemView.button_plan_stats.setOnClickListener { onStatsClicked(mealPlan.plan) }

            // Load image
            if (URLUtil.isValidUrl(mealPlan.plan.imageURL)) {
                Glide.with(itemView.context).asDrawable().load(mealPlan.plan.imageURL)
                    .placeholder(R.drawable.ic_launcher_background).priority(Priority.HIGH)
                    .into(itemView.recipe_plan_picture)
            } else {
                Glide.with(itemView).load(R.drawable.ic_launcher_background)
                    .into(itemView.recipe_plan_picture)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.manage_plans_view_holder, parent, false)
        return PlansViewHolder(
            itemView = itemView,
            onCardClicked = onCardClicked,
            onEditClicked = onEditClicked,
            onDeleteClicked = onDeleteClicked,
            onUnsubscribeClicked = onUnsubscribeClicked,
            onScheduleClicked = onScheduleClicked,
            onStatsClicked = onStatsClicked
        )
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: PlansViewHolder, position: Int) =
        holder.bind(data[position])
}