package edu.hm.foodweek.shopping.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import kotlinx.android.synthetic.main.shoppinglist_item.view.*

data class ShoppingItem(
    val ingredientAmount: IngredientAmount,
    var checked: Boolean = false
)

class ShoppingAdapter(
    var itemList: MutableList<ShoppingItem> = mutableListOf()
) : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    //The viewHolder specify what to do with that view
    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ShoppingItem) {
            itemView.shoppinglist_tv_title.text = item.ingredientAmount.ingredient.name
            itemView.shoppinglist_tv_measure.text = item.ingredientAmount.measure
            itemView.checkbox_ingredient.isChecked = item.checked
            itemView.checkbox_ingredient.setOnClickListener {
                item.checked = !item.checked
            }
        }
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shoppinglist_item, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    //this gets the number specifically associated with each item, so it gets all items from the list
    //Binding each individual item to the view
    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

}