package com.hycinth.loadsir.target;

import android.view.View;
import android.view.ViewGroup;

import com.hycinth.loadsir.callback.Callback;
import com.hycinth.loadsir.callback.SuccessCallback;
import com.hycinth.loadsir.core.LoadLayout;

public class ViewTarget implements ITarget {

    @Override
    public boolean equals(Object target) {
        return target instanceof View;
    }

    @Override
    public LoadLayout replaceView(Object target, Callback.OnReloadListener onReloadListener) {
        View oldContent = (android.view.View) target;
        ViewGroup contentParent = (ViewGroup) (oldContent.getParent());
        int childIndex = 0;
        int childCount = contentParent == null ? 0 : contentParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (contentParent.getChildAt(i) == oldContent) {
                childIndex = i;
                break;
            }
        }
        if (contentParent != null) {
            contentParent.removeView(oldContent);
        }
        ViewGroup.LayoutParams oldLayoutParams = oldContent.getLayoutParams();
        LoadLayout loadLayout = new LoadLayout(oldContent.getContext(), onReloadListener);
        loadLayout.setupSuccessLayout(new SuccessCallback(oldContent, oldContent.getContext(),onReloadListener));
        if (contentParent != null) {
            contentParent.addView(loadLayout, childIndex, oldLayoutParams);
        }
        return loadLayout;
    }
}

