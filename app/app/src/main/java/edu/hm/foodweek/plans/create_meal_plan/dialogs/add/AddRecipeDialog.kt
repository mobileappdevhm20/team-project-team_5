package edu.hm.foodweek.plans.create_meal_plan.dialogs.add


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.create_meal_plan.CreateMealPlanViewModel
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.plans.screen.EndlessScrollListener
import edu.hm.foodweek.recipes.persistence.model.Recipe
import kotlinx.android.synthetic.main.add_recipe_dialog.view.*

class AddRecipeDialog(val createMealPlanViewModel: CreateMealPlanViewModel) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflater = inflater.inflate(R.layout.add_recipe_dialog, container, false)
        setButtonClick(inflater)
        setRecylcerView(inflater)
        setMealTimeSpinner(inflater)
        setDaySpinner(inflater)
        setSelectedDayView(inflater)
        setSearchView(inflater)
        createMealPlanViewModel.searchQuery.observe(viewLifecycleOwner, Observer {
            createMealPlanViewModel.loadMoreRecipes(0)
        })
        return inflater
    }

    private fun setSelectedDayView(inflater: View) {
        createMealPlanViewModel.selectedRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                inflater.add_recipe_dialog_selected_title.text = it.title
                inflater.add_recipe_dialog_selected_description.text = it.description
            } else {
                inflater.add_recipe_dialog_selected_title.text = getString(R.string.no_recipe_selected)
                inflater.add_recipe_dialog_selected_description.text = getString(R.string.no_recipe_selected)
            }
        })
    }

    private fun setSearchView(inflater: View) {
        inflater.add_recipe_dialog_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val input = query?.trim()
                createMealPlanViewModel._searchQuery.postValue(input)
                inflater.add_recipe_dialog_searchbar.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val input = newText?.trim()
                if (input.isNullOrEmpty()) {
                    createMealPlanViewModel._searchQuery.postValue("")
                } else {
                    createMealPlanViewModel._searchQuery.postValue(input)
                }
                return true
            }
        })
    }

    private fun setMealTimeSpinner(inflater: View) {
        val mealtimes = MealTime.values().toList()
        inflater.add_recipe_dialog_time_spinner.adapter = TimeSpinnerAdapter(requireContext(), mealtimes)
        inflater.add_recipe_dialog_time_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                createMealPlanViewModel.selectTime.postValue(inflater.add_recipe_dialog_time_spinner.adapter.getItem(position) as MealTime)
            }

        }
        createMealPlanViewModel.selectTime.observe(viewLifecycleOwner, Observer {
            inflater.add_recipe_dialog_time_spinner.setSelection(mealtimes.indexOf(it))
        })
    }

    private fun setDaySpinner(inflater: View) {
        val weekDays = WeekDay.values().toList()
        inflater.add_recipe_dialog_day_spinner.adapter = DaySpinnerAdapter(requireContext(), weekDays)
        inflater.add_recipe_dialog_day_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                createMealPlanViewModel.selectedDay.postValue(inflater.add_recipe_dialog_day_spinner.adapter.getItem(position) as WeekDay)
            }

        })
        createMealPlanViewModel.selectedDay.observe(viewLifecycleOwner, Observer {
            inflater.add_recipe_dialog_day_spinner.setSelection(weekDays.indexOf(it))
        })
    }

    private fun setButtonClick(inflater: View) {
        inflater.btn_add_selected_recipe.setOnClickListener {
            val selectedRecipe = (inflater.add_recipe_dialog_recylcerView.adapter as RecipeAdapter).getselectedRecipe()
            if (selectedRecipe != null && selectedRecipe.recipeId != 0L) {
                createMealPlanViewModel.addMeal(
                    Recipe(
                        recipeId = selectedRecipe.recipeId,
                        title = selectedRecipe.title,
                        description = selectedRecipe.description,
                        url = selectedRecipe.url
                    )
                )
                Log.i("AddRecipeDialog", "added: $selectedRecipe")
                createMealPlanViewModel.selectedRecipe.postValue(null)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "please select an Recipe", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setRecylcerView(inflater: View) {
        inflater.add_recipe_dialog_recylcerView.adapter = RecipeAdapter(emptyList(), createMealPlanViewModel)
        inflater.add_recipe_dialog_recylcerView.layoutManager = LinearLayoutManager(context)
        createMealPlanViewModel.allRecipes.observe(viewLifecycleOwner, Observer {
            (inflater.add_recipe_dialog_recylcerView.adapter as RecipeAdapter).updateRecipes(it)
        })
        inflater.add_recipe_dialog_recylcerView.addOnScrollListener(object : EndlessScrollListener(pageSize = 30, preLoadSize = 10) {
            override fun loadPage(page: Int) {
                createMealPlanViewModel.loadMoreRecipes(page)
            }

        })
    }
}

