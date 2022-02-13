package com.qapital.ui.activities.state

import com.qapital.base.state.PartialState

class PageLoading(private val swipeRefreshing: Boolean) : PartialState<ActivitiesState> {

    override fun reduceState(previousState: ActivitiesState): ActivitiesState {
        val loading = true.takeIf { !swipeRefreshing } ?: previousState.loading
        val swipeLoading = true.takeIf { swipeRefreshing } ?: previousState.swipeLoading

        return previousState.copy(loading = loading, swipeLoading = swipeLoading)
    }
}