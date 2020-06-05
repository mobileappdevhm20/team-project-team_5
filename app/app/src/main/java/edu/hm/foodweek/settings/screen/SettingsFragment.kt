package edu.hm.foodweek.settings.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import edu.hm.foodweek.BuildConfig
import edu.hm.foodweek.R
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        textView.text = settingsViewModel.text
        if (BuildConfig.DEBUG) {
            val navController = findNavController(this)
            root.btn_nav_dev_settings.setOnClickListener {
                navController.navigate(R.id.action_navigation_settings_to_developerSettingsFragment)
            }
        } else {
            root.btn_nav_dev_settings.visibility = View.GONE
        }
        return root
    }
}
