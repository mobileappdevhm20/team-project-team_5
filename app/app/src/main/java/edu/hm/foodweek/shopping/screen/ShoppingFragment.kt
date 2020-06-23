package edu.hm.foodweek.shopping.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.hm.foodweek.R
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import kotlinx.android.synthetic.main.fragment_shopping.*

class ShoppingFragment : Fragment() {

    private lateinit var shoppingViewModel: ShoppingViewModel
    //private lateinit var listOfIngredient : Ingredient

    var myIngredientList = mutableListOf(IngredientAmount(Ingredient("Butter"), "1"))
    val adapter = ShoppingAdapter(myIngredientList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shoppingViewModel =
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        val listView = root.findViewById<RecyclerView>(R.id.rv_ingredients)
        rv_ingredients?.layoutManager = LinearLayoutManager(requireContext())
        listView.adapter = adapter
        adapter.myIngredientList.add(IngredientAmount(Ingredient("Butter"), "1"))
        adapter.notifyDataSetChanged()


        /* val textView: TextView = root.findViewById(R.id.text_notifications)
         shoppingViewModel.text.observe(viewLifecycleOwner, Observer {
             textView.text = it
         })*/
        return root
    }
}
