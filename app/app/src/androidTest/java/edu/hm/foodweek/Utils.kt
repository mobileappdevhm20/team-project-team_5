package edu.hm.foodweek

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions

fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null
    override fun getDescription() = "Click on a child view with specified id."
    override fun perform(uiController: UiController, view: View) = ViewActions.click()
        .perform(uiController, view.findViewById<View>(viewId))
}