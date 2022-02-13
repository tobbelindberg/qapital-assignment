package com.qapital.base.state

interface PartialState<T : State> {
    fun reduceState(previousState: T): T
}
