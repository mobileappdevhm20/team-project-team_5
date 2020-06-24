package edu.hm.foodweek.shopping.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import kotlinx.android.synthetic.main.shoppinglist_item.view.*

class ShoppingAdapter(
    var myIngredientList: MutableList<IngredientAmount> = mutableListOf()
) : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    //The viewHolder specify what to do with that view
    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ingredientAmount: IngredientAmount) {
            itemView.shoppinglist_tv_title.text = ingredientAmount.ingredient.name
            itemView.shoppinglist_tv_measure.text = ingredientAmount.measure
        }
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shoppinglist_item, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return myIngredientList.size
    }

    //this gets the number specifically associated with each item, so it gets all items from the list
    //Binding each individual item to the view
    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        holder.bind(myIngredientList[position])
    }

}