package com.railian.mvicore.utils.dsl

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

class Alert(
    @StringRes title: Int,
    @StringRes description: Int?,
    alertContext: Context?,
    private val positiveButton: ((DialogInterface) -> Unit)?,
    private val negativeButton: ((DialogInterface) -> Unit)?,
    private val onDismiss: (() -> Unit)?,
) {

    var dialog: AlertDialog? = null

    constructor(builder: Builder) : this(
        builder.title,
        builder.description,
        builder.alertContext,
        builder.positiveButton,
        builder.negativeButton,
        builder.onDismiss
    )

    init {
        if (alertContext != null) {
            AlertDialog.Builder(alertContext).apply {
                setTitle(title)
                if (description != null)
                    setMessage(description)
                setCancelable(false)
                if (positiveButton != null)
                    setPositiveButton("OK") { dialog, _ -> positiveButton.invoke(dialog) }
                if (negativeButton != null)
                    setNegativeButton("Cancel") { dialog, _ -> negativeButton.invoke(dialog) }
                if (onDismiss != null)
                    setOnDismissListener { onDismiss.invoke() }
                dialog = this.show()
            }
        }
    }

    companion object {
        inline fun alert(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var title: Int = 0
        var description: Int? = null
        var alertContext: Context? = null
        var positiveButton: ((DialogInterface) -> Unit)? = null
        var negativeButton: ((DialogInterface) -> Unit)? = null
        var onDismiss: (() -> Unit)? = null
        fun build() = Alert(this)
    }
}