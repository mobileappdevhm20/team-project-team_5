package edu.hm.foodweek.plans.screen.create


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.CreateMealPlanFragmentBinding
import edu.hm.foodweek.plans.persistence.model.MealTime
import edu.hm.foodweek.plans.persistence.model.WeekDay
import edu.hm.foodweek.plans.screen.create.dialogs.add.AddRecipeDialog
import edu.hm.foodweek.plans.screen.create.dialogs.submit.SubmitDialog
import edu.hm.foodweek.recipes.persistence.model.Recipe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CreateMealPlanFragment : Fragment() {

    private val args: CreateMealPlanFragmentArgs by navArgs()
    private val viewModel: CreateMealPlanViewModel by viewModel { parametersOf(args.mealPlanId) }
    private lateinit var binding: CreateMealPlanFragmentBinding
    private var myList = ArrayList<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("CreateMealPlanFragment", "mealPlan by args: ${args.mealPlanId}")

        binding = DataBindingUtil.inflate(
            inflater, R.layout.create_meal_plan_fragment, container, false
        )

        viewModel.selectedDay.observe(viewLifecycleOwner, Observer {
            binding.radioG1.clearCheck()
            when (it) {
                WeekDay.MONDAY -> (binding.radioG1[0] as RadioButton).isChecked = true
                WeekDay.TUESDAY -> (binding.radioG1[1] as RadioButton).isChecked = true
                WeekDay.WEDNESDAY -> (binding.radioG1[2] as RadioButton).isChecked = true
                WeekDay.THURSDAY -> (binding.radioG1[3] as RadioButton).isChecked = true
                WeekDay.FRIDAY -> (binding.radioG1[4] as RadioButton).isChecked = true
                WeekDay.SATURDAY -> (binding.radioG1[5] as RadioButton).isChecked = true
                WeekDay.SUNDAY -> (binding.radioG1[6] as RadioButton).isChecked = true
                else -> (binding.radioG1[0] as RadioButton).isChecked = true
            }
        })

        viewModel.selectTime.observe(viewLifecycleOwner, Observer {
            binding.radioG2.clearCheck()
            when (it) {
                MealTime.BREAKFAST -> (binding.radioG2[0] as RadioButton).isChecked = true
                MealTime.LUNCH -> (binding.radioG2[1] as RadioButton).isChecked = true
                MealTime.DINNER -> (binding.radioG2[2] as RadioButton).isChecked = true
                else -> (binding.radioG2[0] as RadioButton).isChecked = true
            }
        })

        binding.lifecycleOwner = viewLifecycleOwner

        binding.bMonday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.MONDAY)
        }
        binding.bTuesday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.TUESDAY)
        }
        binding.bWednesday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.WEDNESDAY)
        }
        binding.bThursday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.THURSDAY)
        }
        binding.bFriday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.FRIDAY)
        }
        binding.bSaturday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.SATURDAY)
        }
        binding.bSunday.setOnClickListener {
            viewModel.selectedDay.postValue(WeekDay.SUNDAY)
        }

        binding.imageView1.setOnClickListener {
            viewModel.selectTime.postValue(MealTime.BREAKFAST)
        }

        binding.imageView2.setOnClickListener {
            viewModel.selectTime.postValue(MealTime.LUNCH)

        }
        binding.imageView3.setOnClickListener {
            viewModel.selectTime.postValue(MealTime.DINNER)
        }

        val myAdapter =
            CreateMealPlanAdapter(
                requireContext(),
                myList,
                viewModel
            )
        viewModel.filteredRecipeList.observe(viewLifecycleOwner, Observer
        {
            if (it == null || it.isNullOrEmpty()) {
                myAdapter.myList = emptyList()
            } else {
                myAdapter.myList = it
            }
            myAdapter.notifyDataSetChanged()
        })
        binding.btnEnter.setOnClickListener {
            onAddRecipe()
        }
        binding.floatingActionButton.setOnClickListener {
            onSubmit()
        }

        binding.recipeListView.adapter = myAdapter
        binding.recipeListView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    private fun onAddRecipe() {
        AddRecipeDialog(viewModel)
            .show(parentFragmentManager, "AddRecipeDialog")
    }

    private fun onSubmit() {
        SubmitDialog(viewModel)
            .show(parentFragmentManager, "SubmitDialog")
    }

}


