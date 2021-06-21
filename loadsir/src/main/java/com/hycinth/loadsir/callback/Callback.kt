package com.hycinth.loadsir.callback

import android.content.Context
import android.view.View
import java.io.*

abstract class Callback : Serializable {
    constructor()

    private var rootView: View? = null
    private var context: Context? = null
    private var onReloadListener: ((View) -> Unit)? = null
    private var successViewVisible = false
    constructor(view: View, context: Context, onReloadListener: ((View) -> Unit)?) {
        this.context = context
        this.rootView = view
        this.onReloadListener = onReloadListener
    }

    fun setCallback(context: Context, onReloadListener: (View) -> Unit): Callback {
        this.context = context
        this.onReloadListener = onReloadListener
        return this
    }

    fun getRootView(): View? {
        val resId = onCreateView()
        if (resId == 0 && rootView != null) return rootView
        if (onBuildView(context) != null) {
            rootView = onBuildView(context)
        }
        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null)
        }
        rootView?.setOnClickListener {
            if (onReloadEvent(context, rootView)) {
                return@setOnClickListener
            }
            onReloadListener?.invoke(it)
        }
        onViewCreate(context, rootView!!)
        return rootView
    }

    protected open fun onBuildView(context: Context?): View? {
        return null
    }

    /**
     * if return true, the successView will be visible when the view of callback is attached.
     */
    fun getSuccessVisible(): Boolean {
        return successViewVisible
    }

    open fun setSuccessVisible(visible: Boolean) {
        successViewVisible = visible
    }

    protected open fun onReloadEvent(context: Context?, view: View?): Boolean {
        return false
    }

    fun copy(): Callback {
        val bao = ByteArrayOutputStream()
        val oos: ObjectOutputStream?
        var obj: Any? = null
        try {
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback
    }

    open fun obtainRootView(): View {
        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null)
        }
        return rootView!!
    }

    protected abstract fun onCreateView(): Int

    /**
     * Called immediately after [.onCreateView]
     *
     * @since 1.2.2
     */
    protected open fun onViewCreate(context: Context?, view: View) {}

    fun onAttach(context: Context?, view: View?) {}
    fun onDetach() {}
}