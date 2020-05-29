package edu.hm.foodweek.developersettings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.recipes.RecipeDetailFragment
import edu.hm.foodweek.settings.screen.SettingsViewModel
import edu.hm.foodweek.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_developer_settings.view.*
import java.lang.StringBuilder

class DeveloperSettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = InjectorUtils.provideSettingsViewModel(this)
        val rootView = inflater.inflate(R.layout.fragment_developer_settings, container, false)
        navController = NavHostFragment.findNavController(this)

        // dropdown
        setSpinnerAdapter(rootView)
        setAndObserveSpinnerValues(rootView)
        // selected MealPlan
        oberserveSelectedMealPlan(rootView)
        // recipe TextView
        observeRecipesForSelectedMealPlan(rootView)
        // Button
        setButtonClick(rootView)
        return rootView
    }

    private fun setButtonClick(rootView: View) {
        rootView.btn_add_meal.setOnClickListener {
            settingsViewModel.createMeal()
        }

        rootView.btn_go_to_recipe.setOnClickListener {
            val bundle = RecipeDetailFragment.createBundle(1L)
            navController.navigate(R.id.action_developerSettingsFragment_to_recipeDetailFragment, bundle)
        }
    }

    private fun observeRecipesForSelectedMealPlan(rootView: View) {
        settingsViewModel.selectedRecipes.observe(viewLifecycleOwner, Observer {
            val stringBuilder = StringBuilder("")
            for (recipe in it.distinct()) {
                stringBuilder.append(recipe).append("\n\n")
            }
            rootView.recipe_list_text_view.text = stringBuilder.toString()
        })
    }

    private fun oberserveSelectedMealPlan(rootView: View) {
        settingsViewModel.selectedMealPlan.observe(viewLifecycleOwner, Observer {
            rootView.meal_plan_text_view.text = it?.toString() ?: "No Meal-plan selected"
        })
    }

    private fun setAndObserveSpinnerValues(rootView: View) {
        settingsViewModel.allMealPlans.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                rootView.meal_plan_spinner.visibility = View.GONE
            } else {
                rootView.meal_plan_spinner.visibility = View.VISIBLE
                rootView.meal_plan_spinner.adapter = SpinnAdapter(this.requireContext(), it)
            }
        })
    }

    private fun setSpinnerAdapter(rootView: View) {
        rootView.meal_plan_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //settingsViewModel.selectedIndex.postValue(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                settingsViewModel.selectedIndex.postValue(position)
            }

        }
    }
}

private open class SpinnAdapter(val context: Context, val mealPlans: List<MealPlan>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = mealPlans[position].let { it.title + it.planId.toString() } }
    }

    override fun getItem(position: Int): Any {
        return mealPlans[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = mealPlans[position].let { it.title + it.planId.toString() } }
    }

    override fun getCount(): Int {
        return mealPlans.size
    }

}