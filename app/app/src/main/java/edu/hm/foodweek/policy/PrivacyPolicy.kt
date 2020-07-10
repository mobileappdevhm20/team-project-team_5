package edu.hm.foodweek.policy

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import edu.hm.foodweek.R
import edu.hm.foodweek.databinding.PrivacyPolicyBinding
import java.io.InputStream


class PrivacyPolicy : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Apply data binding
        val binding = DataBindingUtil.inflate<PrivacyPolicyBinding>(
            inflater,
            R.layout.privacy_policy,
            container,
            false
        )

        insertPolicyIntoTextView(binding.privacyPolicyOverall, R.raw.privacy_policy)
        insertPolicyIntoTextView(binding.privacyGooglePlay, R.raw.privacy_policy_google_play)
        insertPolicyIntoTextView(binding.privacySecurity, R.raw.privacy_policy_security)
        insertPolicyIntoTextView(binding.privacyChildren, R.raw.privacy_policy_children)
        insertPolicyIntoTextView(binding.privacyInfo, R.raw.privacy_policy_info)

        return binding.root
    }

    private fun insertPolicyIntoTextView(textView: TextView, policy: Int) {
        try {
            val res: Resources = resources
            val in_s: InputStream = res.openRawResource(policy)
            val b = ByteArray(in_s.available())
            in_s.read(b)
            textView.setText(String(b))
        } catch (e: Exception) {
            textView.setText("Error: can't show terms.")
        }
    }
}