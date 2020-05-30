package edu.hm.foodweek.week.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.hm.foodweek.R
import edu.hm.foodweek.util.InjectorUtils

class WeekFragment : Fragment() {

    private lateinit var viewModel: WeekViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = InjectorUtils.provideWeekViewModel(this)
        return inflater.inflate(R.layout.fragment_week, container, false)
    }
}
