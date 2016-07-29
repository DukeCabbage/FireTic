package com.cabbage.firetic.ui.gameboard;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TableLayout;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.Constants;
import com.cabbage.firetic.ui.uiUtils.MyAnimatorListener;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class GameboardSector extends CardView {

    Order.HorizontalOrder horizontalOrder = Order.HorizontalOrder.CENTER;
    Order.VerticalOrder verticalOrder = Order.VerticalOrder.CENTER;
    @BindColor(R.color.white_smoke) int colorWhiteSmoke;
    @BindColor(R.color.divider) int colorDivider;
    @BindColor(R.color.player1) int colorPlayer1;
    @BindColor(R.color.player2) int colorPlayer2;

    @BindDimen(R.dimen.sector_default_elevation) float defaultElevation;

    @BindView(R.id.sectorTableLayout) TableLayout mTable;
    @BindViews({R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8})
    List<View> gridList;

    private Gameboard mGameboard;
    private boolean zoomedIn = false;

    private int mBoardIndex = Constants.Invalid;

    public GameboardSector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGameboard(Gameboard gameboard, int boardIndex) {
        this.mGameboard = gameboard;
        this.setOnClickListener(mGameboard);
        this.mBoardIndex = boardIndex;
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8})
    void onClick(View view) {
        String viewTag = String.valueOf(view.getTag());
        Timber.w("Child on click: %s", viewTag);

        int gridIndex = Integer.valueOf(viewTag);
        int moveMadeBy = mGameboard.onUserClick(mBoardIndex, gridIndex);
        placeMove(gridIndex, moveMadeBy);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        enableGridClick(false);
    }

    void enableGridClick(boolean enable) {
        zoomedIn = enable;
    }

    boolean placeMove(int gridIndex, int player) {
        View grid = gridList.get(gridIndex);
        if (player == Constants.Player1Token) {
            grid.setBackgroundColor(colorPlayer1);
        } else if (player == Constants.Player2Token) {
            grid.setBackgroundColor(colorPlayer2);
        } else if (player == Constants.OpenGrid) {
            grid.setBackgroundColor(colorWhiteSmoke);
        }

        return true;
    }

    void setLocalWinner(int player) {
        if (player == Constants.Player1Token) {
            this.mTable.setBackgroundColor(colorPlayer1);
        } else if (player == Constants.Player2Token) {
            this.mTable.setBackgroundColor(colorPlayer2);
        } else {
            this.mTable.setBackgroundColor(colorDivider);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !zoomedIn || super.onInterceptTouchEvent(ev);
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
                this.setPivotX((int) (width * 0.1));
                break;
            case RIGHT:
                this.setPivotX((int) (width * 0.9));
                break;
            default:
                this.setPivotX(width / 2);
        }

        switch (verticalOrder) {
            case TOP:
                this.setPivotY((int) (height * 0.1));
                break;
            case BOTTOM:
                this.setPivotY((int) (height * 0.9));
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
