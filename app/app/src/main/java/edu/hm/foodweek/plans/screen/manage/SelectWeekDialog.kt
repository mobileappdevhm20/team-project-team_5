package edu.hm.foodweek.plans.screen.manage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.SelectCalendarWeekBinding
import edu.hm.foodweek.plans.screen.MealPlanViewModel
import edu.hm.foodweek.users.persistence.UserRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class SelectWeekDialog(val planId: Long) : DialogFragment() {

    private val userRepository: UserRepository by inject()
    private val mealPlanViewModel: MealPlanViewModel by inject()
    private lateinit var assignedWeekMap: MutableMap<Int, Long>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SelectCalendarWeekBinding>(inflater, R.layout.select_calendar_week, container, false)
        binding.errorMessage = getString(R.string.week_assigned_already)
        val user = userRepository.getUser()
        user.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            assignedWeekMap = it.weekMealPlanMap as MutableMap<Int, Long>
            if (assignedWeekMap.containsKey(binding.numberPicker.value)) {
                updateValues(binding.numberPicker.value, binding)
            }
        })

        binding.numberPicker.let {
            val thisWeek = Calendar.getInstance()
            thisWeek.set(Calendar.DAY_OF_WEEK, thisWeek.firstDayOfWeek)
            it.minValue = thisWeek.get(Calendar.WEEK_OF_YEAR)
            it.maxValue = thisWeek.getActualMaximum(Calendar.WEEK_OF_YEAR)
            it.value = it.minValue
            binding.weekStart.text = buildDate(thisWeek)
            thisWeek.add(Calendar.WEEK_OF_YEAR, 1)
            thisWeek.add(Calendar.DATE, -1)
            binding.weekEnd.text = buildDate(thisWeek)

            it.setOnValueChangedListener { picker, oldVal, newVal ->
                updateValues(newVal, binding)
            }
        }

        binding.assignButton.setOnClickListener {
            lifecycleScope.launch {
                val week = binding.numberPicker.value
                userRepository.setMealToWeek(planId, week)
                Toast.makeText(requireContext(), "Assigned plan to Week $week", Toast.LENGTH_LONG).show()
                dismiss()
            }
        }
        return binding.root
    }

    private fun updateValues(newWeek: Int, binding: SelectCalendarWeekBinding) {
        val selectedWeek = Calendar.getInstance()
        selectedWeek.set(Calendar.DAY_OF_WEEK, selectedWeek.firstDayOfWeek)
        selectedWeek.set(Calendar.WEEK_OF_YEAR, newWeek)
        binding.weekStart.text = buildDate(selectedWeek)
        selectedWeek.add(Calendar.WEEK_OF_YEAR, 1)
        selectedWeek.add(Calendar.DATE, -1)
        binding.weekEnd.text = buildDate(selectedWeek)
        if (assignedWeekMap.containsKey(newWeek)) {
            val mealPlan = mealPlanViewModel.managedPlans.value?.filter {
                it.plan.planId == assignedWeekMap[newWeek]
            }?.map { it.plan.title }
            if (mealPlan.isNullOrEmpty()) {
                binding.errorMessage = getString(R.string.week_assigned_already)
            } else {
                binding.errorMessage = getString(R.string.week_assigned_already) + " by \n${mealPlan[0]}"
            }
            binding.warning.visibility = View.VISIBLE
        } else {
            binding.warning.visibility = View.INVISIBLE
        }
    }


    private fun buildDate(date: Calendar): String {
        return StringBuilder("")
            .append((date.get(Calendar.DATE)).toString()).append(".")
            .append((date.get(Calendar.MONTH) + 1).toString()).append(".")
            .append(date.get(Calendar.YEAR).toString())
            .toString()
    }
}
