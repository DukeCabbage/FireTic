package com.cabbage.firetic.ui.lounge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cabbage.firetic.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class LoungeFragment extends Fragment {

    private Unbinder unbinder;
    @BindColor(R.color.primary) int colorPrimary;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_games) RecyclerView rvGames;
    @BindView(R.id.empty_state) ViewGroup emptyState;
    @BindView(R.id.fab_create_game) FloatingActionButton fabCreateGame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lounge, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        swipeRefreshLayout.setColorSchemeColors(colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop");
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    public static LoungeFragment newInstance() {
        return new LoungeFragment();
    }
}
