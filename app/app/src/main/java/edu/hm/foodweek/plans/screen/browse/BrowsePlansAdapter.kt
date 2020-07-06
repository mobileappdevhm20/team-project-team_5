package edu.hm.foodweek.plans.screen.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import edu.hm.foodweek.R
import kotlinx.android.synthetic.main.browse_plans_view_holder.view.*

class BrowsePlansAdapter(
    private val onCardClicked: (Long) -> Unit,
    private val onSubscribePlanClicked: (BrowseableMealPlan) -> Unit
) : RecyclerView.Adapter<BrowsePlansAdapter.PlansViewHolder>() {

    var data = mutableListOf<BrowseableMealPlan>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PlansViewHolder(
        itemView: View,
        private val onCardClicked: (Long) -> Unit,
        private val onSubscribePlanClicked: (BrowseableMealPlan) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(browseablePlan: BrowseableMealPlan) {
            val mealPlan = browseablePlan.plan
            if (browseablePlan.subscribed) {
                Glide.with(itemView).load(R.drawable.ic_favorite_red_24dp)
                    .into(itemView.subscribe_button)
            } else {
                Glide.with(itemView).load(R.drawable.ic_favorite_black_24dp)
                    .into(itemView.subscribe_button)
            }
            itemView.recipe_plan_text.text = mealPlan.title

            if (URLUtil.isValidUrl(mealPlan.imageURL)) {
                Glide.with(itemView.context).asDrawable().load(mealPlan.imageURL)
                    .placeholder(R.drawable.no_image).priority(Priority.HIGH)
                    .into(itemView.recipe_plan_picture)
            } else {
                Glide.with(itemView).load(R.drawable.no_image)
                    .into(itemView.recipe_plan_picture)
            }

            // Forward to details view on image click
            itemView.recipe_plan_picture.setOnClickListener {
                onCardClicked(mealPlan.planId)
            }

            // Add click listener to subscribe meal plan
            itemView.subscribe_button.setOnClickListener {
                onSubscribePlanClicked(browseablePlan)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.browse_plans_view_holder, parent, false)
        return PlansViewHolder(
            itemView,
            onCardClicked,
            onSubscribePlanClicked
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