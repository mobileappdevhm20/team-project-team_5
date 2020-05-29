package edu.hm.foodweek.plans.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import edu.hm.foodweek.R

class PlanFragment : Fragment() {

    private lateinit var planViewModel: PlanViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        planViewModel =
                ViewModelProviders.of(this).get(PlanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_plan, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        val dummyBtn: Button = root.findViewById(R.id.planDetailsBtn)
        dummyBtn.setOnClickListener {
            val action = PlanFragmentDirections.startPlanDetails(2)
            findNavController().navigate(action)
        }
        planViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
