package edu.hm.foodweek.plans.create_meal_plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.android.synthetic.main.list_item.view.*

class CreateMealPlanAdapter(private var context: Context, var myList: List<Recipe>, private val createMealPlanViewModel: CreateMealPlanViewModel) :
    RecyclerView.Adapter<CreateMealPlanAdapter.ViewHolder>() {

    //The fucntion that creates the view, so this references exactly to which view the recycle view is going to be using for each of its items

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val myListItem = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false)
        return ViewHolder(myListItem)
    }

    //How many items are going to be in the recyclerview
    override fun getItemCount(): Int {
        return myList.count()
    }

    //this gets the number specifically associated with each item, so it gets all items from the list
    //Binding each individual item to the view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(myList[position])
        viewHolder.itemView.setOnClickListener { }
    }


    //The viewHolder specify what to do with that view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(recipe: Recipe) {
            itemView.tv_title.text = recipe.title
            itemView.tv_title.setOnClickListener {
                createMealPlanViewModel.removeMeal(recipe.recipeId)
            }
        }


    }
}