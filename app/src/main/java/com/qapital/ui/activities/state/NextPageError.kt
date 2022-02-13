package com.qapital.ui.activities.state

import com.qapital.data.model.RichActivity
import com.qapital.utils.ConsumableState
import com.qapital.utils.paging.PagePartialState

class NextPageError(private val error: Throwable, override val nextPage: List<RichActivity>? = null, override val nextPageLoading: Boolean = false) :
    PagePartialState<RichActivity, ActivitiesState> {

    override fun reduceState(previousState: ActivitiesState): ActivitiesState {

        return previousState.copy(nextPageError = ConsumableState.of(error), nextPageLoading = nextPageLoading)
    }
}