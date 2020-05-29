package edu.hm.foodweek.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.FragmentRecipeNotFoundBinding
import edu.hm.foodweek.databinding.RecipeDetailFragmentBinding
import edu.hm.foodweek.recipes.persistence.model.Ingredient
import edu.hm.foodweek.util.InjectorUtils
import kotlinx.android.synthetic.main.ingredient_list_item.view.*
import kotlinx.android.synthetic.main.step_list_item.view.*
import java.lang.StringBuilder

class RecipeDetailFragment : Fragment() {

    private lateinit var viewModel: RecipeDetailViewModel
    private var recipeId = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = InjectorUtils.provideRecipeDetailViewModel(this)
        recipeId = arguments?.getLong(RECIPE_ID_PARAM) ?: 0L
        Log.d("RecipeDetailFragment", "recipe Id: $recipeId")
        viewModel.recipeId.postValue(recipeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (recipeId == 0L) {
            val binding = DataBindingUtil.inflate<FragmentRecipeNotFoundBinding>(layoutInflater, R.layout.fragment_recipe_not_found, container, false)
            binding.model = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
            return binding.root
        }
        val binding = DataBindingUtil.inflate<RecipeDetailFragmentBinding>(layoutInflater, R.layout.recipe_detail_fragment, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recipeIngredientsList.apply {
            adapter = IngredientAdapter(emptyList())
            layoutManager = LinearLayoutManager(context)
        }
        binding.recipeStepsList.apply {
            adapter = StepAdapter(emptyList())
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.ingredients.observe(viewLifecycleOwner, Observer { (binding.recipeIngredientsList.adapter as IngredientAdapter).updateIngredients(it) })
        viewModel.steps.observe(viewLifecycleOwner, Observer { (binding.recipeStepsList.adapter as StepAdapter).updateSteps(it) })
        viewModel.title.observe(viewLifecycleOwner, Observer {
            activity?.title = it.toString()
        })
        Glide
            .with(binding.imagePreview.context)
            .load(getString(R.string.default_recipe_image))
            .into(binding.imagePreview)
            .onLoadFailed(resources.getDrawable(R.drawable.ic_no_image_found, null))

        return binding.root
    }

    companion object {
        @JvmStatic
        val RECIPE_ID_PARAM = "recipeId"

        fun createBundle(recipeId: Long): Bundle {
            return bundleOf(RECIPE_ID_PARAM to recipeId)
        }
    }
}


class IngredientAdapter(private var ingredients: List<Ingredient>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val ingredientItemView = inflater.inflate(R.layout.ingredient_list_item, parent, false)
        // Return a new holder instance
        return IngredientItemViewHolder(ingredientItemView)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.itemView.apply {
            amount.text = ingredient.amount.toString()
            ingredient_name.text = ingredient.name
        }
    }

    fun updateIngredients(ingredients: List<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    class IngredientItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

class StepAdapter(private var steps: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val stepItemView = inflater.inflate(R.layout.step_list_item, parent, false)
        // Return a new holder instance
        return StepItemViewHolder(stepItemView)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val step = steps[position]
        holder.itemView.textView.text = StringBuilder((position + 1).toString()).append(". ").append(step.trim()).toString()
    }

    fun updateSteps(steps: List<String>) {
        this.steps = steps
        notifyDataSetChanged()
    }

    class StepItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}