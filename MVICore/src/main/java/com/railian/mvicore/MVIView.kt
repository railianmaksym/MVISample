package com.railian.mvicore

interface MVIView<in Event : UiEvent, in State : UiState, in Effect : UiEffect> {
    fun emit(event: Event)
    fun renderState(state: State)
    fun reactOn(effect: Effect)
}