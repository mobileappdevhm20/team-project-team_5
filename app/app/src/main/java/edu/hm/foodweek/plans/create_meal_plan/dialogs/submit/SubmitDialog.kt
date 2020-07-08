package edu.hm.foodweek.plans.create_meal_plan.dialogs.submit


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

        createMealPlanViewModel.imageUrl.distinctUntilChanged()
            .observe(viewLifecycleOwner, Observer {
                view.submit_dialog_image_preview.visibility = if (it.isNullOrEmpty()) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }

                Glide
                    .with(view.submit_dialog_image_preview)
                    .asDrawable()
                    .load(it)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (!it.isNullOrBlank()) {
                                Toast.makeText(
                                    context,
                                    "URL is no valid image!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    })
                .placeholder(R.drawable.no_image)
                .centerCrop()
                .priority(Priority.HIGH)
                .into(view.submit_dialog_image_preview)
        })

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
        view.submit_dialog_text_view_description.doOnTextChanged { text, _, _, _ ->
            createMealPlanViewModel.updateTexts(description = text.toString())
        }
        view.submit_dialog_text_view_title.doOnTextChanged { text, _, _, _ ->
            createMealPlanViewModel.updateTexts(title = text.toString())
        }
        view.submit_dialog_text_view_url.doOnTextChanged { text, _, _, _ ->
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
