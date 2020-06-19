package edu.hm.foodweek.plans.screen

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(
    private var linearLayoutManager: LinearLayoutManager? = null,
    private val pageSize: Int = 10,
    private val preLoadSize: Int = 2
) : RecyclerView.OnScrollListener() {

    var loading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (linearLayoutManager == null)
            linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val count = linearLayoutManager!!.itemCount
        val i = linearLayoutManager!!.findLastCompletelyVisibleItemPosition()

        if (i > count - preLoadSize) {
            loadPage(i / pageSize + 1)
        }
    }

    abstract fun loadPage(page: Int)
}