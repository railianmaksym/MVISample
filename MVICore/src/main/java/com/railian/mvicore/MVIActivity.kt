package com.railian.mvicore

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.railian.mvicore.utils.dsl.Alert
import kotlinx.coroutines.flow.collect

abstract class MVIActivity<Event : UiEvent, State : UiState, Effect : UiEffect>(
    @LayoutRes contentLayoutId: Int
) : AppCompatActivity(contentLayoutId), MVIView<Event, State, Effect> {

    abstract val feature: Feature<Event, State, Effect>

    private var currentAlertOnScreen: Alert? = null

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

    fun showAlertDialog(
        @StringRes titleResource: Int,
        @StringRes descriptionResource: Int? = null,
        onPositiveClicked: ((DialogInterface) -> Unit)? = null,
        onNegativeClicked: ((DialogInterface) -> Unit)? = null
    ) {
        if (currentAlertOnScreen != null) return
        currentAlertOnScreen = Alert.alert {
            alertContext = this@MVIActivity
            title = titleResource
            description = descriptionResource
            positiveButton = onPositiveClicked
            negativeButton = onNegativeClicked
            onDismiss = {
                currentAlertOnScreen = null
            }
        }
    }

}