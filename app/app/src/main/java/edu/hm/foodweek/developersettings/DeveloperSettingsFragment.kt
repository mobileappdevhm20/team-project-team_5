package edu.hm.foodweek.developersettings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.persistence.model.MealPlan
import edu.hm.foodweek.settings.screen.SettingsViewModel
import edu.hm.foodweek.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_developer_settings.view.*

class DeveloperSettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = InjectorUtils.provideSettingsViewModel(this)
        val rootView = inflater.inflate(R.layout.fragment_developer_settings, container, false)

        rootView.meal_plan_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                settingsViewModel.selectedIndex.postValue(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("DevSettings", "selected : $position")
                settingsViewModel.selectedIndex.postValue(position)
            }

        }
        settingsViewModel.selectedMeal.observe(viewLifecycleOwner, Observer {
            if (it != null)
                rootView.meal_plan_text_view.text = it.toString()
        })
        settingsViewModel.allMealPlans.observe(viewLifecycleOwner, Observer {
            Log.d("DevSettings", "$it")
            if (!it.isNullOrEmpty())
                rootView.meal_plan_spinner.adapter = SpinnAdapter(this.requireContext(), it)
        })

        settingsViewModel.selectedRecipes.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                for (recipe in it) {
                    rootView.recipe_list_text_view.text = recipe.toString() + "\n"
                }
            }
        })

        settingsViewModel.allRecipes.observe(viewLifecycleOwner, Observer {
            Log.d("DevSettings", "all recipes: $it")
        })

        rootView.btn_add_meal.setOnClickListener {
            settingsViewModel.createMeal()
        }
        return rootView
    }
}

private open class SpinnAdapter(val context: Context, val mealPlans: List<MealPlan>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = mealPlans[position].let {it.title + it.planId.toString() } }
    }

    override fun getItem(position: Int): Any {
        return mealPlans[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = mealPlans[position].let {it.title + it.planId.toString() } }
    }

    override fun getCount(): Int {
        return mealPlans.size
    }

}