package edu.hm.foodweek.plans.screen.manage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.create_meal_plan.CreateMealPlanViewModel
import edu.hm.foodweek.users.persistence.UserRepository
import kotlinx.android.synthetic.main.mealplan_submition.view.*
import kotlinx.android.synthetic.main.select_calendar_week.view.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.StringBuilder
import java.util.*
import java.util.logging.Logger

class SelectWeekDialog(val planId: Long) : DialogFragment() {

    val userRepository: UserRepository by inject()
    lateinit var assignedWeekMap: MutableMap<Int, Long>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.select_calendar_week, container, false)

        val assignedWeeks = userRepository.getUser()
        assignedWeeks.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            assignedWeekMap = it.weekMealPlanMap as MutableMap<Int, Long>
            Logger.getLogger("SelectWeekDialog").fine("weekMealPlanUpdate: $assignedWeekMap")
        })

        view.number_picker.let {
            val thisWeek = Calendar.getInstance(Locale.GERMANY)
            val selectedWeek = Calendar.getInstance(Locale.GERMANY)
            it.minValue = thisWeek.get(Calendar.WEEK_OF_YEAR)
            it.maxValue = 52
            it.value = thisWeek.get(Calendar.WEEK_OF_YEAR)
            it.setOnValueChangedListener { picker, oldVal, newVal ->
                selectedWeek.set(Calendar.WEEK_OF_YEAR, newVal)
                view.week_start.text = buildDate(selectedWeek, 0)
                view.week_end.text = buildDate(selectedWeek, 7)
                if (assignedWeekMap.containsKey(newVal))
                    view.warning.visibility = View.VISIBLE
                else
                    view.warning.visibility = View.INVISIBLE
            }
        }
        view.assign_button.setOnClickListener {
            lifecycleScope.launch {
                val week = view.number_picker.value
                userRepository.setMealToWeek(planId,week)
                Toast.makeText(requireContext(),"Assigned plan to Week $week",Toast.LENGTH_LONG).show()
                dismiss()
            }
        }
        return view
    }


    private fun buildDate(date: Calendar, day: Int): String {
        return StringBuilder("")
            .append((date.get(Calendar.DATE) + day).toString()).append(".")
            .append(date.get(Calendar.MONTH).toString()).append(".")
            .append(date.get(Calendar.YEAR).toString())
            .toString()

    }
}
