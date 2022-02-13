package com.qapital.ui.activities.state

import com.qapital.base.state.PartialState

class PageError(private val error: Throwable) : PartialState<ActivitiesState> {

    override fun reduceState(previousState: ActivitiesState): ActivitiesState {

        return previousState.copy(pageError = error, loading = false, swipeLoading = false)
    }
}