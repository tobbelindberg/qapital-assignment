package com.qapital.ui.activities.state

import com.qapital.data.model.RichActivity
import com.qapital.utils.paging.PagePartialState
import okhttp3.internal.toImmutableList


class NextPageLoaded(override val nextPage: List<RichActivity>?, override val nextPageLoading: Boolean = false) :
    PagePartialState<RichActivity, ActivitiesState> {

    override fun reduceState(previousState: ActivitiesState): ActivitiesState {
        val nextPage = nextPage ?: emptyList()

        return previousState.copy(activities = previousState.activities.toImmutableList().plus(nextPage), nextPageLoading = nextPageLoading)
    }
}