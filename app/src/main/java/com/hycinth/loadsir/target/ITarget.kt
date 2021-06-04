package com.hycinth.loadsir.target

import android.view.View
import com.hycinth.loadsir.core.LoadLayout

interface ITarget {
    fun replaceView(target: Any,onReloadListener:((View)->Unit)?):LoadLayout
}