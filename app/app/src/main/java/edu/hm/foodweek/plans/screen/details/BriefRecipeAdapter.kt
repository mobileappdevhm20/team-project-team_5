package edu.hm.foodweek.plans.screen.details

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.vipulasri.timelineview.TimelineView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.android.synthetic.main.plan_day_details_recipe_view_holder.view.*

class BriefRecipeAdapter(
    private val mFeedList: List<Recipe>,
    private val request: RequestBuilder<Drawable>,
    private val onRecipeClicked: (Recipe) -> Unit
) : RecyclerView.Adapter<BriefRecipeAdapter.RecipeBriefListViewHolder>() {
    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeBriefListViewHolder {
        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return RecipeBriefListViewHolder(
            mLayoutInflater.inflate(
                R.layout.plan_day_details_recipe_view_holder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeBriefListViewHolder, position: Int) {
        val recipe = mFeedList[position]
        holder.recipeTitle.text = recipe.title
        holder.recipeDescription.text = recipe.description.take(50) + "..."
        holder.recipeCooktime.text = "Cookingtime: n/a"
        holder.recipePreparationTime.text = "Preparationtime: n/a"

        holder.imageView.setOnClickListener {
            onRecipeClicked(recipe)
        }

        if (recipe.url.isNotBlank()) {
            request.load(recipe.url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .priority(Priority.HIGH)
                .transition(withCrossFade(1000))
                .circleCrop()
                .into(holder.imageView)
        }

    }

    override fun getItemCount() = mFeedList.size

    inner class RecipeBriefListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.plan_day_details_recipe_view_holder_imageView
        val recipeTitle: TextView = itemView.recipe_title
        val recipeDescription: TextView = itemView.recipe_description
        val recipePreparationTime: TextView = itemView.recipe_preparation_time
        val recipeCooktime: TextView = itemView.recipe_cooking_time
    }

}