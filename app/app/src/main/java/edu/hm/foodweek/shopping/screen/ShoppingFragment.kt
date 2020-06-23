package edu.hm.foodweek.shopping.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentShoppingBinding
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class ShoppingFragment : Fragment(), KoinComponent {

    private val shoppingViewModel: ShoppingViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Apply data binding
        val binding = DataBindingUtil.inflate<FragmentShoppingBinding>(
            inflater,
            R.layout.fragment_shopping,
            container,
            false
        )
        binding.viewModel = shoppingViewModel

        // Set own adapter to recycler view
        val adapter = ShoppingAdapter()
        val recyclerView = binding.rvIngredients
        recyclerView.adapter = adapter

        // Populate items to rv
        shoppingViewModel.ingredients.observe(viewLifecycleOwner, Observer { ingredientAmounts ->
            if (ingredientAmounts != null) {
                adapter.myIngredientList = ingredientAmounts.toMutableList()
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }
}
