package com.cabbage.firetic.ui.uiUtils;

import android.animation.Animator;
import android.view.View;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class MyAnimatorListener implements Animator.AnimatorListener {

    final WeakReference<View> wfTargetView;

    public MyAnimatorListener(WeakReference<View> wfView) {
        wfTargetView = wfView;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        View view = wfTargetView.get();
        if (view != null) {
            float currentScale = view.getScaleX();
            float x = view.getX();
            float pivotX = view.getPivotX();
            int height = view.getHeight();
            Timber.i("Anim start with: ");
            Timber.i("Scale: %f, x: %f, pivot: %f, height: %d", currentScale, x, pivotX, height);
        }
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        View view = wfTargetView.get();
        if (view != null) {
            float currentScale = view.getScaleX();
            float x = view.getX();
            float pivotX = view.getPivotX();
            int height = view.getHeight();
            Timber.i("Anim end with: ");
            Timber.i("Scale: %f, x: %f, pivot: %f, height: %d", currentScale, x, pivotX, height);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {}

    @Override
    public void onAnimationRepeat(Animator animator) {}
}