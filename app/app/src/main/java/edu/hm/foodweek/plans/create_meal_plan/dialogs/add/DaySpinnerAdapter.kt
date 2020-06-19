package edu.hm.foodweek.plans.create_meal_plan.dialogs.add

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import edu.hm.foodweek.plans.persistence.model.WeekDay

class DaySpinnerAdapter(
    val context: Context,
    private val weekDay: List<WeekDay>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = weekDay[position].toString() }
    }

    override fun getItem(position: Int): Any {
        return weekDay[position]
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = weekDay[position].toString() }
    }

    override fun getCount(): Int {
        return weekDay.size
    }
}