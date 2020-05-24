package edu.hm.foodweek.week.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import edu.hm.foodweek.R
import edu.hm.foodweek.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_week.view.*
import java.lang.StringBuilder

class WeekFragment : Fragment() {

    private lateinit var viewModel: WeekViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = InjectorUtils.provideWeekViewModel(this)
        val root = inflater.inflate(R.layout.fragment_week, container, false)
        setOnClicks(root)
        setObservers(root)
        return root
    }

    private fun setObservers(root: View) {
        viewModel.allMealPlans.observe(viewLifecycleOwner, Observer {
            val sb = StringBuilder()
            for (mealPlan in it) {
                sb.append(mealPlan.toString())
                sb.append("\n")
            }
            root.list_meals.text = sb.toString()
        })
        viewModel.allRecipes.observe(viewLifecycleOwner, Observer {
            val sb = StringBuilder()
            for (recipe in it) {
                sb.append(recipe.toString())
                sb.append("\n")
            }
            root.list_recipe.text = sb.toString()
        })
    }

    private fun setOnClicks(root: View) {
        root.btn_add_recipe.setOnClickListener {
            viewModel.createRecipe()
        }
    }
}
