package edu.hm.foodweek.week.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentWeekBinding
import edu.hm.foodweek.plans.persistence.model.Meal
import edu.hm.foodweek.plans.screen.details.PlanDayDetailsFragmentDirections
import kotlinx.android.synthetic.main.meal_preview_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeekFragment : Fragment() {

    private val viewModel: WeekViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWeekBinding>(layoutInflater, R.layout.fragment_week, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.planId.observe(viewLifecycleOwner, Observer { id ->
            binding.weekPreviewShowPlan.setOnClickListener { view ->
                val action = WeekFragmentDirections.startPlanDetails(id)
                findNavController().navigate(action)
            }
        })
        setImage(binding)
        setRecyclerView(binding)
        return binding.root
    }

    private fun setRecyclerView(binding: FragmentWeekBinding) {
        binding.weekPreviewRecipeList.adapter = MealPreviewAdapter(emptyList(), onRecipeClicked)
        binding.weekPreviewRecipeList.layoutManager = LinearLayoutManager(context)
        viewModel.meals.observe(viewLifecycleOwner, Observer {
            it?.let {
                (binding.weekPreviewRecipeList.adapter as MealPreviewAdapter).updateMealPlans(it)
            }
        })
    }

    private fun setImage(binding: FragmentWeekBinding) {
        viewModel.planUrl.observe(viewLifecycleOwner, Observer {
            Glide
                .with(binding.weekPreviewImage)
                .asDrawable()
                .load(it)
                .placeholder(R.drawable.ic_no_image_found)
                .centerCrop()
                .priority(Priority.HIGH)
                .into(binding.weekPreviewImage)
        })
    }

    val onRecipeClicked = { recipeId: Long ->
        val action = PlanDayDetailsFragmentDirections.showRecipeDetail(recipeId)
        findNavController().navigate(action)
        Unit
    }

    class MealPreviewAdapter(
        private var meals: List<Meal>,
        private val onRecipeClicked: (Long) -> Unit
    ) : RecyclerView.Adapter<MealPreviewAdapter.IngredientItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientItemViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            // Inflate the custom layout
            val ingredientItemView = inflater.inflate(R.layout.meal_preview_item, parent, false)
            // Return a new holder instance
            return IngredientItemViewHolder(ingredientItemView, onRecipeClicked)
        }

        override fun getItemCount(): Int {
            return meals.size
        }

        override fun onBindViewHolder(holder: IngredientItemViewHolder, position: Int) {
            val meal = meals[position]
            holder.bind(meal)

        }

        fun updateMealPlans(meals: List<Meal>) {
            this.meals = meals
            notifyDataSetChanged()
        }

        class IngredientItemViewHolder(
            itemView: View,
            private val onRecipeClicked: (Long) -> Unit
        ) : RecyclerView.ViewHolder(itemView) {
            fun bind(meal: Meal) {
                itemView.apply {
                    setOnClickListener {
                        onRecipeClicked(meal.recipe.recipeId)
                    }
                    mealpreview_header.text = meal.recipe.title
                    mealpreview_time.text = meal.time.toString()
                    mealpreview_date.text = meal.day.toString()
                    if (URLUtil.isValidUrl(meal.recipe.url)) {
                        Glide
                            .with(itemView)
                            .asDrawable()
                            .load(meal.recipe.url)
                            .placeholder(R.drawable.ic_no_image_found)
                            .priority(Priority.HIGH)
                            .circleCrop()
                            .into(mealpreview_image)

                    }
                }
            }
        }
    }
}