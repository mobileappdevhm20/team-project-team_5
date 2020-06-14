package edu.hm.foodweek.plans.screen

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val pageSize: Int = 10,
    private val preLoadSize: Int = 2
) : RecyclerView.OnScrollListener() {

    var loading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val count = linearLayoutManager.itemCount
        val i = linearLayoutManager.findLastCompletelyVisibleItemPosition()

        if (i > count - preLoadSize) {
            loadPage(count / pageSize + 1)
        }
    }

    abstract fun loadPage(page: Int)
}