package edu.hm.foodweek.plans.create_meal_plan.dialogs.submit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.create_meal_plan.CreateMealPlanViewModel
import edu.hm.foodweek.plans.persistence.model.MealPlan
import kotlinx.android.synthetic.main.mealplan_submition.view.*

class SubmitDialog(private val createMealPlanViewModel: CreateMealPlanViewModel) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mealplan_submition, container, false)

        val currentMealPlan = createMealPlanViewModel.currentEditingMealPlan.value
        view.submit_dialog_text_view_title.setText(currentMealPlan?.title ?: "")
        view.submit_dialog_text_view_description.setText(currentMealPlan?.description ?: "")
        view.submit_dialog_text_view_url.setText(currentMealPlan?.imageURL ?: "")

        view.submit_dialog_btn_publish.setOnClickListener {
            currentMealPlan?.let {
                if (checkValues(it)) {
                    dismiss()
                    requireParentFragment().findNavController().popBackStack()
                    createMealPlanViewModel.publishMealPlan()
                    Toast.makeText(context, "Plan Published", Toast.LENGTH_LONG).show()
                }
            }
        }

        view.submit_dialog_btn_draft.setOnClickListener {
            currentMealPlan?.let {
                if (checkValues(it)) {
                    dismiss()
                    requireParentFragment().findNavController().popBackStack()
                    createMealPlanViewModel.saveDraft()
                    Toast.makeText(context, "Plan saved as Draft", Toast.LENGTH_LONG).show()
                }
            }
        }
        view.submit_dialog_text_view_description.doOnTextChanged { text, start, before, count ->
            createMealPlanViewModel.updateTexts(description = text.toString())
        }
        view.submit_dialog_text_view_title.doOnTextChanged { text, start, before, count ->
            createMealPlanViewModel.updateTexts(title = text.toString())
        }
        view.submit_dialog_text_view_url.doOnTextChanged { text, start, before, count ->
            createMealPlanViewModel.updateTexts(url = text.toString())
        }
        return view
    }

    private fun checkValues(mealPlan: MealPlan): Boolean {
        val title = mealPlan.title
        val description = mealPlan.description
        val meals = mealPlan.meals
        when {
            title.isNullOrEmpty() -> {
                Toast.makeText(context, "Title should not be empty", Toast.LENGTH_LONG).show()
                return false
            }
            description.isNullOrEmpty() -> {
                Toast.makeText(context, "Description should not be empty", Toast.LENGTH_LONG).show()
                return false
            }
            meals.isNullOrEmpty() -> {
                Toast.makeText(context, "Please assign at least one meal", Toast.LENGTH_LONG).show()
                return false
            }
            else -> return true
        }
    }
}
