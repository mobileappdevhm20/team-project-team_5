package edu.hm.foodweek.shopping.screen

import androidx.lifecycle.ViewModel

class ShoppingItemViewModel() : ViewModel() {
    var checked: Boolean = false
    fun onClick() {
        checked = !checked
    }
}