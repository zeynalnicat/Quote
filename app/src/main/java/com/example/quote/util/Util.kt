package com.example.quote.util

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.snackbar.Snackbar

class Util(private val context: Context, private val appView: View) {

    fun copyClipboard(view: View, content: String) {
        ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.5f).apply {
            duration = 400
            start()
        }
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", content)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(appView, "Copied", Snackbar.LENGTH_SHORT).show()
        ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f).apply {
            duration = 400
            start()
        }

    }


}