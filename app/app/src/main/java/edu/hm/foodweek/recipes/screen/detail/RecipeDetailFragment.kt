package edu.hm.foodweek.recipes.screen.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.RecipeDetailFragmentBinding
import edu.hm.foodweek.recipes.persistence.model.IngredientAmount
import kotlinx.android.synthetic.main.ingredient_list_item.view.*
import kotlinx.android.synthetic.main.step_list_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RecipeDetailFragment : Fragment() {
    private val args: RecipeDetailFragmentArgs by navArgs()
    private val viewModel: RecipeDetailViewModel by viewModel { parametersOf(args.recipeId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("RecipeDetailFragment", "recipeId by args: ${args.recipeId}")
        val binding = DataBindingUtil.inflate<RecipeDetailFragmentBinding>(layoutInflater, R.layout.recipe_detail_fragment, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recipeIngredientsList.apply {
            adapter =
                IngredientAdapter(emptyList())
            layoutManager = LinearLayoutManager(context)
        }
        binding.recipeStepsList.apply {
            adapter =
                StepAdapter(emptyList())
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.ingredients.observe(viewLifecycleOwner, Observer { (binding.recipeIngredientsList.adapter as IngredientAdapter).updateIngredients(it) })
        viewModel.steps.observe(viewLifecycleOwner, Observer { (binding.recipeStepsList.adapter as StepAdapter).updateSteps(it) })
        viewModel.title.observe(viewLifecycleOwner, Observer {
            activity?.title = it.toString()
        })
        viewModel.url.observe(viewLifecycleOwner, Observer {
            Glide
                .with(binding.imagePreview.context)
                .asDrawable()
                .load(it)
                .placeholder(R.drawable.no_image)
                .priority(Priority.HIGH)
                .circleCrop()
                .into(binding.imagePreview)
        })

        return binding.root
    }
}


class IngredientAdapter(private var ingredientAmountList: List<IngredientAmount>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val ingredientItemView = inflater.inflate(R.layout.ingredient_list_item, parent, false)
        // Return a new holder instance
        return IngredientItemViewHolder(
            ingredientItemView
        )
    }

    override fun getItemCount(): Int {
        return ingredientAmountList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ingredient = ingredientAmountList[position]
        holder.itemView.apply {
            amount.text = ingredient.measure
            ingredient_name.text = ingredient.ingredient.name
        }
    }

    fun updateIngredients(ingredientAmountList: List<IngredientAmount>) {
        this.ingredientAmountList = ingredientAmountList
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
        return StepItemViewHolder(
            stepItemView
        )
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