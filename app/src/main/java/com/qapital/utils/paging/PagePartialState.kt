package com.qapital.utils.paging

import com.qapital.base.state.PartialState
import com.qapital.base.state.State
import com.qapital.data.TimestampItem

interface PagePartialState<E : TimestampItem, T : State> : PartialState<T> {
    val nextPage: List<E>?
    val nextPageLoading: Boolean
}