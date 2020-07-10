package edu.hm.foodweek.plans.screen.create.dialogs.add

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import edu.hm.foodweek.plans.persistence.model.MealTime

class TimeSpinnerAdapter(
    val context: Context,
    private val mealTimes: List<MealTime>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = mealTimes[position].toString() }
    }

    override fun getItem(position: Int): Any {
        return mealTimes[position]
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return TextView(context).apply { text = mealTimes[position].toString() }
    }

    override fun getCount(): Int {
        return mealTimes.size
    }
}