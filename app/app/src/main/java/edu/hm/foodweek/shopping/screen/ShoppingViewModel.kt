package edu.hm.foodweek.shopping.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingViewModel : ViewModel() {

    private val _list = MutableLiveData<String>().apply {


        // value = "This is shopping Fragment"


    }

    //val text: LiveData<String> = _text
    val list: LiveData<String> = _list
}