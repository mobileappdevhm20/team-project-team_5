package edu.hm.foodweek.plans.screen.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.persistence.model.MealPlan
import kotlinx.android.synthetic.main.browse_plans_view_holder.view.*

class BrowsePlansAdapter(
    private val onCardClicked: (Long) -> Unit
) : RecyclerView.Adapter<BrowsePlansAdapter.PlansViewHolder>() {

    var data = mutableListOf<MealPlan>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PlansViewHolder(
        itemView: View,
        private val onCardClicked: (Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(mealPlan: MealPlan) {
            itemView.recipe_plan_text.text = mealPlan.title
            Glide.with(itemView.context).asDrawable().load(mealPlan.imageURL)
                .placeholder(R.drawable.ic_launcher_background).priority(Priority.HIGH)
                .into(itemView.recipe_plan_picture)

            // Forward to details view on image click
            itemView.recipe_plan_picture.setOnClickListener {
                onCardClicked(mealPlan.planId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.browse_plans_view_holder, parent, false)
        return PlansViewHolder(
            itemView,
            onCardClicked
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PlansViewHolder, position: Int) =
        holder.bind(data[position])
}