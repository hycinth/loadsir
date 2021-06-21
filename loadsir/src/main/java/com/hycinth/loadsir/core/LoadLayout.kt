package com.hycinth.loadsir.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.hycinth.loadsir.LoadSirUtil
import com.hycinth.loadsir.callback.Callback
import com.hycinth.loadsir.callback.SuccessCallback

class LoadLayout : FrameLayout {
    private var onReloadListener: ((View) -> Unit)? = null
    private var curCallback: Class<out Callback>? = null
    private var preCallback: Class<out Callback>? = null
    private val callbacks: HashMap<Class<out Callback>, Callback> = hashMapOf()

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1

    }

    constructor(context: Context) : super(context)
    constructor(context: Context, onReloadListener: ((View) -> Unit)?) : this(context) {
        this.onReloadListener = onReloadListener
    }

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        successView!!.visibility = INVISIBLE
        addView(
            successView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        curCallback = SuccessCallback::class.java
    }

    fun setupCallback(callback: Callback) {
        val cloneCallback = callback.copy()
        onReloadListener?.let { cloneCallback.setCallback(context, it) }
        addCallback(cloneCallback)
    }

    fun addCallback(callback: Callback) {
        if (!callbacks.containsKey(callback.javaClass)) {
            callbacks[callback.javaClass] = callback
        }
    }

    fun showCallback(callback: Class<out Callback>) {
        checkCallbackExist(callback)
        if (LoadSirUtil.isMainThread) {
            showCallbackView(callback)
        } else {
            postToMainThread(callback)
        }
    }

    fun getCurrentCallback(): Class<out Callback>? {
        return curCallback
    }

    private fun postToMainThread(status: Class<out Callback>) {
        post { showCallbackView(status) }
    }


    fun showCallbackView(status: Class<out Callback>) {
        if (preCallback != null) {
            if (preCallback == status) {
                return
            }
            callbacks[preCallback]?.onDetach()
        }
        if (childCount > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX)
        }
        callbacks.keys.forEach { key ->
            if (key == status) {
                val successCallback = callbacks[SuccessCallback::class.java] as SuccessCallback
                if (key == SuccessCallback::class.java) {
                    successCallback.showWithCallback(true)
                } else {
                    successCallback.showWithCallback(callbacks[key]!!.getSuccessVisible())
                    val rootView = callbacks[key]!!.getRootView()
                    addView(rootView)
                    callbacks[key]!!.onAttach(context, rootView)
                }
                preCallback = status
            }
        }
        curCallback = status
    }

    fun setCallBack(callback: Class<out Callback>, transport: ((Context, View) -> Unit)?) {
        if (transport == null) {
            return
        }
        checkCallbackExist(callback)
        transport.invoke(context, callbacks[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: Class<out Callback>) {
        require(callbacks.containsKey(callback)) {
            String.format(
                "The Callback (%s) is nonexistent.", callback
                    .simpleName
            )
        }
    }
}