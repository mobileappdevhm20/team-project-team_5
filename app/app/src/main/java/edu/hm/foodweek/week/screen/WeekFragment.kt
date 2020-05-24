package edu.hm.foodweek.week.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import edu.hm.foodweek.R
import edu.hm.foodweek.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_week.view.*

class WeekFragment : Fragment() {

    private lateinit var viewModel: WeekViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = InjectorUtils.provideWeekViewModel(this)
        val root = inflater.inflate(R.layout.fragment_week, container, false)
        root.btn.setOnClickListener {
            viewModel.createOnClick()
        }
        viewModel.allMealPlans.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
                root.next_recipe_text_view.text = it.toString()
        })
        return root
    }
}
