package com.qapital.ui.activities.state

import com.qapital.data.model.RichActivity
import com.qapital.utils.paging.PagePartialState

class NextPageLoading(override val nextPage: List<RichActivity>? = null, override val nextPageLoading: Boolean = true) : PagePartialState<RichActivity, ActivitiesState> {

    override fun reduceState(previousState: ActivitiesState): ActivitiesState {

        return previousState.copy(nextPageLoading = nextPageLoading)
    }
}