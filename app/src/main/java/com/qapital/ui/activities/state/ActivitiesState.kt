package com.qapital.ui.activities.state

import com.qapital.base.state.State
import com.qapital.data.model.RichActivity
import com.qapital.utils.ConsumableState

data class ActivitiesState(
    val activities: List<RichActivity> = emptyList(),
    val pageError: Throwable? = null,
    val loading: Boolean = false,
    val swipeLoading: Boolean = false,
    val nextPageLoading: Boolean = false,
    val nextPageError: ConsumableState<Throwable> = ConsumableState.of()
) : State