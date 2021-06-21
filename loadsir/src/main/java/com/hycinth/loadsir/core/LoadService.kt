package com.hycinth.loadsir.core

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.hycinth.loadsir.callback.Callback
import com.hycinth.loadsir.callback.SuccessCallback

class LoadService<T> {
    private var loadLayout: LoadLayout? = null
    private var convertor: ((T) -> Class<out Callback>)? = null

    constructor(
        convertor: ((T) -> Class<out Callback>)?,
        loadLayout: LoadLayout,
        builder: LoadSir.Builder
    ) {
        this.convertor = convertor
        this.loadLayout = loadLayout
        initCallBack(builder)
    }

    private fun initCallBack(builder: LoadSir.Builder) {
        val callbacks = builder.getCallbacks()
        val defaultCallback = builder.getDefaultCallback()
        if (callbacks.isNotEmpty()) {
            callbacks.forEach {
                loadLayout?.setupCallback(it)
            }
        }
        Handler().post {
            if (defaultCallback != null) {
                loadLayout?.showCallback(defaultCallback)
            }
        }
    }

    fun showSuccess() {
        loadLayout?.showCallback(SuccessCallback::class.java)
    }

    fun showCallback(callback: Class<out Callback>) {
        loadLayout?.showCallback(callback)
    }
    fun showWithConvertor(t:T){
        requireNotNull(convertor) { "You haven't set the Convertor." }
        loadLayout?.showCallback(convertor!!.invoke(t))
    }
    fun getLoadLayout(): LoadLayout? {
        return loadLayout
    }
    fun getCurrentCallback(): Class<out Callback>? {
        return loadLayout?.getCurrentCallback()
    }
    fun getTitleLoadLayout(
        context: Context?,
        rootView: ViewGroup,
        titleView: View?
    ): LinearLayout {
        val newRootView = LinearLayout(context)
        newRootView.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        newRootView.layoutParams = layoutParams
        rootView.removeView(titleView)
        newRootView.addView(titleView)
        newRootView.addView(loadLayout, layoutParams)
        return newRootView
    }
    fun setCallBack(callback: Class<out Callback>, transport: ((Context,View)->Unit)?): LoadService<T> {
        loadLayout?.setCallBack(callback, transport)
        return this
    }
}