package com.railian.mvicore

interface MVIView<Event : UiEvent, in State : UiState, Effect : UiEffect> {
    fun emit(event: Event)
    fun renderState(state: State)
    fun reactOn(effect: Effect)
}