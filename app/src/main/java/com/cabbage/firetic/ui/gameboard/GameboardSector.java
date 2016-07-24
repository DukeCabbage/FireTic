package com.cabbage.firetic.ui.gameboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;

import com.cabbage.firetic.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import timber.log.Timber;

public class GameboardSector extends TableLayout {

    public final int TOTAL_GRID_NUMBER = 9;

    private int currentPlayer = 1;
    private boolean zoomedIn = false;

    @BindColor(R.color.primary) int colorPrimary;
    @BindColor(R.color.player1) int colorPlayer1;
    @BindColor(R.color.player2) int colorPlayer2;

//    @BindView(R.id.touch_interceptor) View touchInterceptor;
    @BindViews({R.id.btn_0, R.id.btn_1, R.id.btn_2,
            R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8})
    List<View> gridList;
    private int[] gridOwnership = new int[TOTAL_GRID_NUMBER];

    public GameboardSector(Context context) {
        super(context);
    }

    public GameboardSector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        enableGridClick(false);
    }

    void enableGridClick(boolean enable) {
        zoomedIn = enable;
        for (View view : gridList) {
            view.setEnabled(enable);
            view.setOnClickListener(enable ? gridOnClickListener : null);
        }

        this.setClickable(!zoomedIn);
//        touchInterceptor.setVisibility(enable ? GONE : VISIBLE);
    }

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

//    int[] getGridOwnership() {
//        return gridOwnership;
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


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !zoomedIn || super.onInterceptTouchEvent(ev);
    }

    List<WeakReference<EventListener>> wfListenerList =  new ArrayList<>();

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

    public interface EventListener {
        void userMoveMade(int player, int location);
    }
}
