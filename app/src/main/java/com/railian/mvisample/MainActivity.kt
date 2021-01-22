package com.railian.mvisample

import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.railian.mvicore.MVIActivity
import com.railian.mvicore.UiEffect
import com.railian.mvicore.UiEvent
import com.railian.mvicore.UiState
import com.railian.mvisample.databinding.ActivityMainBinding


class MainActivity :
    MVIActivity<MainFeature<MainContract.Event, MainContract.LoginState, MainContract.Effect>>(R.layout.activity_main) {

    override val feature: MainFeature<MainContract.Event, MainContract.LoginState, MainContract.Effect>
        get() = ViewModelProvider(this)[MainFeature::class.java] as MainFeature<MainContract.Event, MainContract.LoginState, MainContract.Effect>
    private val viewBinding: ActivityMainBinding by viewBinding(R.id.content)

    override fun onActivityCreated() {
        viewBinding.materialButton.setOnClickListener {
            feature.setEvent(MainContract.Event.OnPhoneAuthClicked(viewBinding.etPhone.text.toString()))
        }

        viewBinding.google.setOnClickListener {
            feature.setEvent(MainContract.Event.OnGoogleAuthClicked)
        }

        viewBinding.facebook.setOnClickListener {
            feature.setEvent(MainContract.Event.OnFacebookAuthClicked)
        }
    }

    override fun emit(event: UiEvent) {
        feature.setEvent(event)
    }

    override fun renderState(state: UiState) {
        viewBinding.progress.isVisible = state.isLoading
        viewBinding.materialButton.isVisible = !state.isLoading
    }

    override fun reactOn(effect: UiEffect) {
        when (effect) {
            MainContract.Effect.OpenPhoneConfirmation -> showToast("Open phone confirmation")
            MainContract.Effect.OpenGoogleLogin -> showToast("Open google login")
            MainContract.Effect.OpenFacebookLogin -> showToast("Open facebook login")
            MainContract.Effect.ShowPhoneValidationError -> viewBinding.textInputLayout.error =
                "Please enter valid phone"
        }
    }
}