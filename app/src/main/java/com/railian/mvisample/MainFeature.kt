package com.railian.mvisample

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.viewModelScope
import com.railian.mvicore.Feature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFeature : Feature<MainContract.Event, MainContract.LoginState, MainContract.Effect>() {

    override fun createInitialState(): MainContract.LoginState {
        return MainContract.LoginState()
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnPhoneAuthClicked -> validatePhone(event.phone)
            MainContract.Event.OnGoogleAuthClicked -> setEffect { MainContract.Effect.OpenGoogleLogin }
            MainContract.Event.OnFacebookAuthClicked -> setEffect { MainContract.Effect.OpenFacebookLogin }
        }
    }

    private fun validatePhone(phone: String) {
        setState { copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.Default) {
            delay(3_000)
            setState { copy(isLoading = false) }
            if (PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
                setEffect { MainContract.Effect.OpenPhoneConfirmation }
            } else {
                setEffect { MainContract.Effect.ShowPhoneValidationError }
            }
        }
    }
}