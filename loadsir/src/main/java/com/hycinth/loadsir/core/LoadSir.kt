package com.hycinth.loadsir.core

import android.view.View
import com.hycinth.loadsir.LoadSirUtil
import com.hycinth.loadsir.callback.Callback
import com.hycinth.loadsir.target.ActivityTarget
import com.hycinth.loadsir.target.ITarget
import com.hycinth.loadsir.target.ViewTarget
import java.util.*

class LoadSir {
    private var builder:Builder?=null
    companion object{
        private var loadSir:LoadSir?=null
        fun getDefault(): LoadSir {
            if (loadSir == null) {
                synchronized(LoadSir::class.java) {
                    if (loadSir == null) {
                        loadSir = LoadSir()
                    }
                }
            }
            return loadSir!!
        }
        fun beginBuilder(): Builder {
            return Builder()
        }
    }
    constructor(){
        this.builder = Builder()
    }
    constructor(builder: Builder){
        this.builder = builder
    }
    private fun setBuilder(builder: Builder){
        this.builder = builder
    }
    fun register(target:Any):LoadService<*>{
        return register<Any>(target,null,null)
    }
    fun register(target: Any, onReloadListener: ((View)->Unit)?): LoadService<*> {
        return register<Any>(target, onReloadListener, null)
    }


    fun <T> register(
        target: Any,
        onReloadListener: ((View)->Unit)?,
        convertor: ((T)-> Class<out Callback>)?
    ): LoadService<T> {
        val targetContext: ITarget =
            LoadSirUtil.getTargetContext(target, builder!!.getTargetContextList())
        val loadLayout = targetContext.replaceView(target, onReloadListener)
        return LoadService(convertor, loadLayout, builder!!)
    }

    class Builder {
        private val callbacks: MutableList<Callback> = ArrayList<Callback>()
        private val targetContextList: MutableList<ITarget> = ArrayList<ITarget>()
        private var defaultCallback: Class<out Callback>? = null
        fun addCallback(callback: Callback): Builder {
            callbacks.add(callback)
            return this
        }

        /**
         * @param targetContext
         * @return Builder
         * @since 1.3.8
         */
        fun addTargetContext(targetContext: ITarget): Builder {
            targetContextList.add(targetContext)
            return this
        }

        fun getTargetContextList(): List<ITarget> {
            return targetContextList
        }

        fun setDefaultCallback(defaultCallback: Class<out Callback>): Builder {
            this.defaultCallback = defaultCallback
            return this
        }

        fun getCallbacks(): List<Callback> {
            return callbacks
        }

        fun getDefaultCallback(): Class<out Callback?>? {
            return defaultCallback
        }

        fun commit() {
            getDefault().setBuilder(this)
        }

        fun build(): LoadSir {
            return LoadSir(this)
        }

        init {
            targetContextList.add(ActivityTarget())
            targetContextList.add(ViewTarget())
        }
    }
}