package edu.hm.foodweek.week.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentWeekBinding
import edu.hm.foodweek.plans.screen.PlanFragmentDirections
import edu.hm.foodweek.week.MealPreview
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
        binding.weekPreviewRecipeList.adapter = MealPreviewAdapter(emptyList())
        binding.weekPreviewRecipeList.layoutManager = LinearLayoutManager(context)
        viewModel.meals.observe(viewLifecycleOwner, Observer {
            (binding.weekPreviewRecipeList.adapter as MealPreviewAdapter).updateMealPlans(it)
        })
    }

    private fun setImage(binding: FragmentWeekBinding) {
        viewModel.planUrl.observe(viewLifecycleOwner, Observer {
            Glide
                .with(binding.weekPreviewImage.context)
                .load(it)
                .centerCrop()
                .into(binding.weekPreviewImage)
                .onLoadFailed(resources.getDrawable(R.drawable.ic_no_image_found, null))
        })
    }

    class MealPreviewAdapter(private var mealPreviews: List<MealPreview>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            // Inflate the custom layout
            val ingredientItemView = inflater.inflate(R.layout.meal_preview_item, parent, false)
            // Return a new holder instance
            return IngredientItemViewHolder(ingredientItemView)
        }

        override fun getItemCount(): Int {
            return mealPreviews.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val mealPreview = mealPreviews[position]
            holder.itemView.apply {
                mealpreview_header.text = mealPreview.recipeName
                mealpreview_time.text = mealPreview.mealTime.toString()
                Glide
                    .with(mealpreview_image.context)
                    .load(mealPreview.url)
                    .circleCrop()
                    .into(mealpreview_image)
                    .onLoadFailed(resources.getDrawable(R.drawable.ic_no_image_found, null))
            }
        }

        fun updateMealPlans(mealPreviews: List<MealPreview>) {
            this.mealPreviews = mealPreviews
            notifyDataSetChanged()
        }

        class IngredientItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}