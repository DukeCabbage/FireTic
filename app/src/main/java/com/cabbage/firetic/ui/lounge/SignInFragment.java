package com.cabbage.firetic.ui.lounge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cabbage.firetic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignInFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.btn_connect) Button btnConnect;

    @OnClick(R.id.btn_connect)
    void connectOnClick(View view) {
        ((LoungeActivity) getActivity()).mockLogin();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }
}
