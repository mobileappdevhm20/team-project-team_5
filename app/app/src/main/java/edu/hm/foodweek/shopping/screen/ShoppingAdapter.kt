package edu.hm.foodweek.shopping.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import kotlinx.android.synthetic.main.shoppinglist_item.view.*

class ShoppingAdapter(var myIngredientList: MutableList<IngredientAmount>) :
    RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.shoppinglist_item, parent, false)
        return ViewHolder(itemView)


    }

    override fun getItemCount(): Int {
        return myIngredientList.size
    }


    //this gets the number specifically associated with each item, so it gets all items from the list
    //Binding each individual item to the view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myIngredientList[position])
    }

    //The viewHolder specify what to do with that view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ingredientAmount: IngredientAmount) {
            itemView.shoppinglist_tv_title.text = ingredientAmount.ingredient.name
        }


    }


}