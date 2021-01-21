package com.railian.mvicore

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

abstract class MVIActivity<S : UiState, E : UiEffect, F : Feature<UiEvent, S, E>>(
    @LayoutRes contentLayoutId: Int
) : AppCompatActivity(contentLayoutId) {

    abstract val feature: F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onActivityCreated()

        lifecycleScope.launchWhenStarted {
            feature.uiState.collect { renderState(it) }
        }

        lifecycleScope.launchWhenStarted {
            feature.effect.collect { reactOn(it) }
        }
    }

    abstract fun onActivityCreated()

    abstract fun renderState(state: S)

    abstract fun reactOn(effect: E)

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

}