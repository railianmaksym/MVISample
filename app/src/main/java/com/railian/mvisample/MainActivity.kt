package com.railian.mvisample

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.railian.mvisample.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private val viewBinding: ActivityMainBinding by viewBinding(R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setLightStatusBar()
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            statusBarColor = Color.TRANSPARENT
        }

        viewBinding.materialButton.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnPhoneAuthClicked(viewBinding.etPhone.text.toString()))
        }

        viewBinding.google.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnGoogleAuthClicked)
        }

        viewBinding.facebook.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnFacebookAuthClicked)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                viewBinding.progress.isVisible = it.isLoading
                viewBinding.materialButton.isVisible = !it.isLoading
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect {
                when (it) {
                    MainContract.Effect.OpenPhoneConfirmation -> showToast("Open phone confirmation")
                    MainContract.Effect.OpenGoogleLogin -> showToast("Open google login")
                    MainContract.Effect.OpenFacebookLogin -> showToast("Open facebook login")
                    MainContract.Effect.ShowPhoneValidationError -> viewBinding.textInputLayout.error =
                        "Please enter valid phone"
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
        }
    }
}