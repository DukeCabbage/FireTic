package com.cabbage.firetic.ui.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabbage.firetic.R;
import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;
import com.google.common.base.Strings;

import butterknife.Unbinder;

public class SignUpFragment extends Fragment
        implements SignUpContract.View {

    private Unbinder unbinder;
//    private SignUpActivity activity;
    private SignUpContract.Presenter mPresenter;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        return rootView;
    }


    @Override
    public void signUpSuccess(@NonNull Player player) {
        DialogHelper.dismissProgressDialog();
    }

    @Override
    public void signUpFail(@Nullable String errMsg) {
        DialogHelper.dismissProgressDialog();

        if (getView() != null) {
            errMsg = Strings.isNullOrEmpty(errMsg) ? "Sign up fail" : errMsg;
            Snackbar.make(getView(), errMsg, Snackbar.LENGTH_LONG).setDuration(10000).show();
        }
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
