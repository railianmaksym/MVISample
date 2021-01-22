package com.railian.mvisample

import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.railian.mvicore.MVIActivity
import com.railian.mvisample.databinding.ActivityMainBinding


class MainActivity :
    MVIActivity<MainContract.Event, MainContract.LoginState, MainContract.Effect>(R.layout.activity_main) {

    override val feature: MainFeature
        get() = ViewModelProvider(this)[MainFeature::class.java]

    private val viewBinding: ActivityMainBinding by viewBinding(R.id.content)

    override fun onActivityCreated() {
        viewBinding.materialButton.setOnClickListener {
            emit(MainContract.Event.OnPhoneAuthClicked(viewBinding.etPhone.text.toString()))
        }

        viewBinding.google.setOnClickListener {
            emit(MainContract.Event.OnGoogleAuthClicked)
        }

        viewBinding.facebook.setOnClickListener {
            emit(MainContract.Event.OnFacebookAuthClicked)
        }
    }

    override fun renderState(state: MainContract.LoginState) {
        viewBinding.progress.isVisible = state.isLoading
        viewBinding.materialButton.isVisible = !state.isLoading
    }

    override fun reactOn(effect: MainContract.Effect) {
        when (effect) {
            MainContract.Effect.OpenPhoneConfirmation -> showToast("Open phone confirmation")
            MainContract.Effect.OpenGoogleLogin -> showToast("Open google login")
            MainContract.Effect.OpenFacebookLogin -> showToast("Open facebook login")
            MainContract.Effect.ShowPhoneValidationError -> viewBinding.textInputLayout.error =
                "Please enter valid phone"
        }
    }
}