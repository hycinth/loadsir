package com.hycinth.loadsir

import android.os.Looper
import com.hycinth.loadsir.target.ITarget

object LoadSirUtil {
    val isMainThread: Boolean = Looper.myLooper() == Looper.getMainLooper()

    fun getTargetContext(target: Any, targetContextList: List<ITarget>): ITarget {
        for (targetContext in targetContextList) {
            if (targetContext == target) {
                return targetContext
            }
        }
        throw IllegalArgumentException("No TargetContext fit it")
    }
}