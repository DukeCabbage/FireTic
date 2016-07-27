package com.cabbage.firetic.ui.gameboard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.MyAnimatorListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindViews;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GameboardSector extends CardView {

    Order.HorizontalOrder horizontalOrder = Order.HorizontalOrder.CENTER;
    Order.VerticalOrder verticalOrder = Order.VerticalOrder.CENTER;

    public final int TOTAL_GRID_NUMBER = 9;
    @BindColor(R.color.primary) int colorPrimary;
    @BindColor(R.color.player1) int colorPlayer1;
    @BindColor(R.color.player2) int colorPlayer2;
    @BindDimen(R.dimen.sector_default_elevation) float defaultElevation;

    @BindViews({R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8})
    List<View> gridList;
    List<WeakReference<EventListener>> wfListenerList = new ArrayList<>();
    private int currentPlayer = 1;
    private boolean zoomedIn = false;
    private int[] gridOwnership = new int[TOTAL_GRID_NUMBER];
    private OnClickListener gridOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            String viewTag = String.valueOf(view.getTag());
            Timber.w("Child on click: %s", viewTag);

            int index = Integer.valueOf(viewTag);
            placeMove(currentPlayer, index);
            if (currentPlayer == 1) {
                currentPlayer++;
            } else {
                currentPlayer--;
            }
        }
    };

    /**
     * Constructor
     */
    public GameboardSector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        enableGridClick(false);
    }

//    int[] getGridOwnership() {
//        return gridOwnership;
//    }

    void enableGridClick(boolean enable) {
        zoomedIn = enable;
        for (View view : gridList) {
            view.setEnabled(enable);
            view.setOnClickListener(enable ? gridOnClickListener : null);
        }

        this.setClickable(!zoomedIn);
    }

//    void placeMoves(int[] moves) {
//        if (moves.length != TOTAL_GRID_NUMBER) {
//            throw new RuntimeException();
//        }
//
//        for (int index = 0; index < TOTAL_GRID_NUMBER; index ++) {
//            int move = moves[index];
//            if (move == 1 || move == 2) {
//                placeMove(move, index);
//            }
//        }
//    }

    void placeMove(int player, int location) {
        if (location < 0 || location >= TOTAL_GRID_NUMBER) {
            throw new RuntimeException();
        }

        gridOwnership[location] = player;
        View grid = gridList.get(location);
        if (player == 1) {
            grid.setBackgroundColor(colorPlayer1);
        } else {
            grid.setBackgroundColor(colorPlayer2);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !zoomedIn || super.onInterceptTouchEvent(ev);
    }

    public void addListener(@NonNull EventListener listener) {
        wfListenerList.add(new WeakReference<>(listener));
    }

    private void notifyListeners(int player, int location) {
        for (WeakReference<EventListener> wfListener : wfListenerList) {
            EventListener listener = wfListener.get();
            if (listener != null) {
                listener.userMoveMade(player, location);
            }
        }
    }

    void focusOnSector(boolean isEnlarging) {
        if (zoomedIn == isEnlarging) {
            return;
        }

        float currentScale = this.getScaleX();
        final ViewPropertyAnimator animator = this.animate();
        int width = this.getWidth();
        int height = this.getHeight();
        switch (horizontalOrder) {
            case LEFT:
                this.setPivotX((int)(width * 0.1));
                break;
            case RIGHT:
                this.setPivotX((int)(width * 0.9));
                break;
            default:
                this.setPivotX(width / 2);
        }

        switch (verticalOrder) {
            case TOP:
                this.setPivotY((int)(height * 0.1));
                break;
            case BOTTOM:
                this.setPivotY((int)(height * 0.9));
                break;
            default:
                this.setPivotY(height / 2);
        }

        if (currentScale == 1 && isEnlarging) {
            float scaleTo = 3.0f;
            animator.scaleX(scaleTo).scaleY(scaleTo);
        } else if (!isEnlarging) {
            animator.scaleX(1).scaleY(1);
        } else {
            return;
        }
        this.enableGridClick(isEnlarging);

        animator.setDuration(333L)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new ElevationSetter(new WeakReference<>((View) this)))
                .start();

        ValueAnimator ani = ValueAnimator.ofFloat(0f, 1f);
        ani.setTarget(this);
        ani.setDuration(333L);
        ani.setInterpolator(new LinearInterpolator());
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float dfdfd = (float) valueAnimator.getAnimatedValue();
                Timber.e(dfdfd + "");
                valueAnimator.tar
            }
        });
        ani.start();
    }

    public interface EventListener {
        void userMoveMade(int player, int location);
    }

    private class ElevationSetter extends MyAnimatorListener {

        public ElevationSetter(WeakReference<View> wfView) {
            super(wfView);
        }

        @Override
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            View view = wfTargetView.get();
            if (view != null) {
                if (view.getScaleX() == 1.0) {
                    view.setElevation(2.0f * defaultElevation);
                }
            }
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            View view = wfTargetView.get();
            if (view != null) {
                if (view.getScaleX() == 1.0) {
                    view.setElevation(defaultElevation);
                }
            }
        }
    }
}
