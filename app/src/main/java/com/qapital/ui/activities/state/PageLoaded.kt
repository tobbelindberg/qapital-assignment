package com.qapital.ui.activities.state

import com.qapital.base.state.PartialState
import com.qapital.data.model.RichActivity

class PageLoaded(private val activities: List<RichActivity>) :
    PartialState<ActivitiesState> {

    override fun reduceState(previousState: ActivitiesState): ActivitiesState {

        return previousState.copy(activities = activities, loading = false, swipeLoading = false, pageError = null)
    }
}