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

        // Setup slider
        val max = 7
        val min = 1
        val total = max - min
        binding.fluidSlider.positionListener = { pos ->
            val day = min + (total * pos).toInt()
            binding.fluidSlider.bubbleText = "$day"
            shoppingViewModel.daySelectionChanged(day)
        }
        binding.fluidSlider.position = .3f
        binding.fluidSlider.startText = "now"
        binding.fluidSlider.endText = "$max days"


        // Populate items to rv
        shoppingViewModel.ingredients.observe(viewLifecycleOwner, Observer { ingredientAmounts ->
            if (ingredientAmounts.isNullOrEmpty()) {
                binding.infoText.visibility = View.VISIBLE
            } else {
                adapter.itemList = ingredientAmounts.map { ShoppingItem(it) }.toMutableList()
                adapter.notifyDataSetChanged()
                binding.infoText.visibility = View.INVISIBLE
            }
        })

        return binding.root
    }
}
