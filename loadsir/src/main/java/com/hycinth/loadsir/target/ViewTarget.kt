package com.hycinth.loadsir.target

import android.view.View
import android.view.ViewGroup
import com.hycinth.loadsir.callback.SuccessCallback
import com.hycinth.loadsir.core.LoadLayout

class ViewTarget:ITarget {
    override fun equals(other: Any?): Boolean {
        return other is View
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
    override fun replaceView(target: Any, onReloadListener: ((View) -> Unit)?): LoadLayout {
        val oldContent = target as View
        val contentParent = oldContent.parent as ViewGroup
        var childIndex = 0
        val childCount = contentParent.childCount
        for (i in 0 until childCount) {
            if (contentParent.getChildAt(i) === oldContent) {
                childIndex = i
                break
            }
        }
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context, onReloadListener)
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent,
                oldContent.context,
                onReloadListener
            )
        )
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}