package edu.hm.foodweek.plans.create_meal_plan.dialogs.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.create_meal_plan.CreateMealPlanViewModel
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.android.synthetic.main.recipe_list_item.view.*

class RecipeAdapter(private var recipes: List<Recipe>, private val createMealPlanViewModel: CreateMealPlanViewModel) : RecyclerView.Adapter<RecipeAdapter.StepItemViewHolder>() {
    var checkedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepItemViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val stepItemView = inflater.inflate(R.layout.recipe_list_item, parent, false)
        // Return a new holder instance
        return StepItemViewHolder(stepItemView)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: StepItemViewHolder, position: Int) {
        holder.bind(recipes[position], position == checkedPosition)
    }

    fun updateRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    fun getselectedRecipe(): Recipe? {
        return createMealPlanViewModel.selectedRecipe.value
    }

    inner class StepItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.recipe_list_item_recipe_text
        fun bind(recipe: Recipe, isSelected: Boolean) {
            this.setIsRecyclable(false)
            //textView.isSelected = isSelected
            textView.text = recipe.title
            itemView.setOnClickListener {
/*                textView.isSelected = true
                checkedPosition = adapterPosition*/
                createMealPlanViewModel.selectedRecipe.postValue(recipe)
                notifyDataSetChanged()
            }
        }
    }
}