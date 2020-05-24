package edu.hm.foodweek.settings.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import edu.hm.foodweek.R
import edu.hm.foodweek.shopping.screen.ShoppingViewModel
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: ShoppingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val navController = findNavController(this)
        root.btn_nav_dev_settings.setOnClickListener {
            navController.navigate(R.id.action_navigation_settings_to_developerSettingsFragment)
        }
        return root
    }
}
