package com.railian.mvicore

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

abstract class MVIActivity<Event : UiEvent, State : UiState, Effect : UiEffect>(
    @LayoutRes contentLayoutId: Int
) : AppCompatActivity(contentLayoutId), MVIView<Event, State, Effect> {

    abstract val feature: Feature<Event, State, Effect>

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

    override fun emit(event: Event) {
        feature.setEvent(event)
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

}