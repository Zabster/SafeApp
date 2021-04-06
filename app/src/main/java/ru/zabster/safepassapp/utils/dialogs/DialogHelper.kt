package ru.zabster.safepassapp.utils.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.google.android.material.button.MaterialButton

import ru.zabster.safepassapp.R

/**
 * Extension for instance of AlertDialog for simple loader
 *
 * @param message pass it if want to change loading message
 *
 * @return instance of [AlertDialog]
 */
@SuppressLint("InflateParams")
fun Context.loadingDialog(message: String? = null): AlertDialog {
    val v = LayoutInflater.from(this).inflate(R.layout.dialog_simple_loading, null)
    v.findViewById<TextView>(R.id.loadingMessage).text = message ?: getString(R.string.dialog_loading_message)
    return AlertDialog.Builder(this).setView(v).create()
}

/**
 * Extension for instance of AlertDialog for alert with actions
 *
 * @param dialogDataModel resource data model
 * @param positiveAction positive action callback
 * @param negativeAction negative action callback
 *
 * @return instance of [AlertDialog]
 */
@SuppressLint("InflateParams")
fun Context.alert(
    dialogDataModel: DialogDataModel,
    positiveAction: (AlertDialog.() -> Unit)? = null,
    negativeAction: (AlertDialog.() -> Unit)? = null
): AlertDialog {
    val v = LayoutInflater.from(this).inflate(R.layout.dialog_base_dialog_with_actions, null)
    val dialog = AlertDialog.Builder(this)
        .setView(v)
        .setCancelable(dialogDataModel.isCancelable)
        .create()

    val messageTextView = v.findViewById<TextView>(R.id.messageText)
    messageTextView.setTextIsSelectable(dialogDataModel.isSelectable)

    v.findViewById<TextView>(R.id.titleText).text = dialogDataModel.title
    messageTextView.text = dialogDataModel.message

    val positiveButton = v.findViewById<MaterialButton>(R.id.positiveButton)
    val negativeButton = v.findViewById<MaterialButton>(R.id.negativeButton)

    dialogDataModel.positiveButtonText?.let { positiveButtonText ->
        positiveButton.text = positiveButtonText
        positiveButton.setOnClickListener { positiveAction?.invoke(dialog) }
    } ?: run { positiveButton.isGone = true }

    dialogDataModel.negativeButtonText?.let { negativeButtonText ->
        negativeButton.text = negativeButtonText
        negativeButton.setOnClickListener { negativeAction?.invoke(dialog) }
    } ?: run { negativeButton.isGone = true }

    return dialog
}

/**
 * Ges bottoms sheet with actions
 *
 * @param title title for bottom sheet
 * @param actions list items
 * @param click click callback with id
 *
 * @return bottom sheet dialog
 */
@SuppressLint("InflateParams")
fun <T> Context.bottomSheet(
    title: String,
    actions: List<Pair<String, T>>,
    click: BottomSheetDialog.(index: Int, chosenData: T) -> Unit
): BottomSheetDialog {
    val padding = resources.getDimensionPixelSize(R.dimen.padding_base_size)
    val view = LayoutInflater.from(this)
        .inflate(R.layout.bottom_sheet_choose_cred_item, null, false)
    return BottomSheetDialog(this).apply {
        view.findViewById<TextView>(R.id.title).text = title
        view.findViewById<LinearLayout>(R.id.actionList).apply {
            actions.forEachIndexed { index, (text, data) ->
                addView(TextView(this@bottomSheet, null, 0, R.style.TextStyle_BaseText).apply {
                    setPadding(padding, padding, padding, padding)
                    this.text = text
                    setOnClickListener { click(index, data) }
                })
            }
        }
        setContentView(view)
    }
}
