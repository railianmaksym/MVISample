package com.railian.mvisample

import com.railian.mvicore.Contract
import com.railian.mvicore.UiEffect
import com.railian.mvicore.UiEvent
import com.railian.mvicore.UiState

class MainContract: Contract {
    // Events that user performed
    sealed class Event : UiEvent {
        data class OnPhoneAuthClicked(val phone: String) : Event()
        object OnGoogleAuthClicked : Event()
        object OnFacebookAuthClicked : Event()
    }

    // Ui View State
    data class LoginState(
        val isLoading: Boolean = false,
        val phone: String = "",
        val error: String = ""
    ) : UiState

    // Side effects
    sealed class Effect : UiEffect {
        object OpenPhoneConfirmation : Effect()
        object OpenGoogleLogin : Effect()
        object OpenFacebookLogin : Effect()
        object ShowPhoneValidationError : Effect()
    }
}