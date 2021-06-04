package com.hycinth.loadsir.callback

import android.R
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StyleRes

class ProgressCallback : Callback {
    private var title: String = ""
    private var subTitle: String = ""
    private var subTitleStyleRes = -1
    private var titleStyleRes = -1

    constructor(builder: Builder) {
        title = builder.title
        subTitle = builder.subTitle
        subTitleStyleRes = builder.subTitleStyleRes
        titleStyleRes = builder.titleStyleRes
        setSuccessVisible(builder.aboveable)
    }

    override fun onCreateView(): Int {
        return 0
    }

    override fun onBuildView(context: Context?): View {
        return LinearLayout(context)
    }

    override fun onViewCreate(context: Context?, view: View) {
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.gravity = Gravity.CENTER
        val ll = view as LinearLayout
        ll.orientation = LinearLayout.VERTICAL
        ll.gravity = Gravity.CENTER
        val progressBar = ProgressBar(context)
        ll.addView(progressBar, lp)
        if (title.isNotEmpty()) {
            val tvTitle = TextView(context)
            tvTitle.text = title
            if (titleStyleRes == -1) {
                tvTitle.setTextAppearance(context, R.style.TextAppearance_Large)
            } else {
                tvTitle.setTextAppearance(context, titleStyleRes)
            }
            ll.addView(tvTitle, lp)
        }
        if (subTitle.isNotEmpty()){
            val tvSubtitle = TextView(context)
            tvSubtitle.text = subTitle
            if (subTitleStyleRes == -1) {
                tvSubtitle.setTextAppearance(context, R.style.TextAppearance_Medium)
            } else {
                tvSubtitle.setTextAppearance(context, subTitleStyleRes)
            }
            ll.addView(tvSubtitle, lp)
        }
    }

    class Builder {
        var title: String = ""
        var subTitle: String = ""
        var subTitleStyleRes = -1
        var titleStyleRes = -1
        var aboveable = false
        fun setTitle(title: String): Builder {
            return setTitle(title, -1)
        }

        fun setTitle(
            title: String,
            @StyleRes titleStyleRes: Int
        ): Builder {
            this.title = title
            this.titleStyleRes = titleStyleRes
            return this
        }

        fun setSubTitle(subTitle: String): Builder {
            return setSubTitle(subTitle, -1)
        }

        fun setSubTitle(
            subTitle: String,
            @StyleRes subTitleStyleRes: Int
        ): Builder {
            this.subTitle = subTitle
            this.subTitleStyleRes = subTitleStyleRes
            return this
        }

        fun setAboveSuccess(aboveable: Boolean): Builder {
            this.aboveable = aboveable
            return this
        }

        fun build(): ProgressCallback {
            return ProgressCallback(this)
        }
    }
}