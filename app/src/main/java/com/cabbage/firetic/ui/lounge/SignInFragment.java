package com.cabbage.firetic.ui.lounge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class SignInFragment extends Fragment {

    private Unbinder unbinder;
    private LoungeActivity activity;

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.btn_connect) Button btnConnect;

    @OnClick(R.id.btn_connect)
    void connectOnClick() {
        String inputUserName = etUsername.getText().toString();
        boolean validUserName = !TextUtils.isEmpty(inputUserName);

        if (validUserName) {
            DialogHelper.showProgressDialog(activity, "Logging in...");
            activity.loungePresenter.login(inputUserName);
        } else {
            etUsername.setError("User name can't be empty");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        activity = (LoungeActivity) getActivity();
        unbinder = ButterKnife.bind(this, rootView);
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

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }
}
