package com.cabbage.firetic.ui.gameboard;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cabbage.firetic.R;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GameboardActivity2 extends AppCompatActivity {

    //region Outlets
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.view_zoom_test) View testView;
    //endregion

    boolean zoomFlag;

    @SuppressWarnings("unused")
    @OnClick(R.id.view_zoom_test)
    void testZoom() {
        float currentScale = testView.getScaleX();

        final ViewPropertyAnimator animator = testView.animate();
        if (currentScale < 3.0) {
            animator.scaleX(currentScale + 1).scaleY(currentScale + 1);
        } else {
            animator.scaleX(1).scaleY(1);
        }

        animator.setDuration(333L)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new MyAnimatorListener(new WeakReference<>(testView)))
                .start();
    }

    public final static class MyAnimatorListener implements Animator.AnimatorListener {

        final WeakReference<View> wfTargetView;

        MyAnimatorListener(WeakReference<View> wfView) {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard_2);
        ButterKnife.bind(this);
        setUpAppBar();
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }
}
