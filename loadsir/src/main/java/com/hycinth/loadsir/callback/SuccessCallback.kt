package com.hycinth.loadsir.callback

import android.content.Context
import android.view.View

class SuccessCallback : Callback {
    override fun onCreateView(): Int {
        return 0
    }

    constructor(view: View, context: Context, onReloadListener: ((View) -> Unit)?)
            : super(view, context, onReloadListener)


    fun showWithCallback(successVisible: Boolean) {
        obtainRootView().visibility = if (successVisible) View.VISIBLE else View.INVISIBLE
    }
}