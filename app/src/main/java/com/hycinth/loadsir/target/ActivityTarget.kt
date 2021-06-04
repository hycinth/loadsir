package com.hycinth.loadsir.target

import android.R
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.hycinth.loadsir.callback.SuccessCallback
import com.hycinth.loadsir.core.LoadLayout

class ActivityTarget:ITarget {
    override fun equals(other: Any?): Boolean {
        return other is Activity
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
    override fun replaceView(target: Any, onReloadListener: ((View) -> Unit)?): LoadLayout {
        val activity = target as Activity
        val contentParent = activity.findViewById<ViewGroup>(R.id.content)
        val childIndex = 0
        val oldContent = contentParent.getChildAt(childIndex)
        contentParent.removeView(oldContent)

        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(activity, onReloadListener)
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent,
                activity,
                onReloadListener
            )
        )
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}