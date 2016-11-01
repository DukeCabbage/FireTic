package com.cabbage.firetic.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cabbage.firetic.R;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;
import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class SignUpFragment extends Fragment
        implements SignUpContract.View {

    @BindView(R.id.et_user_name) EditText etUserName;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_repeat_password) EditText etRepeatPassword;

    private Unbinder unbinder;
    private SignUpContract.Presenter mPresenter;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @OnClick(R.id.btn_sign_up)
    void signUpOnClick(View v) {
        String inputUserName = etUserName.getText().toString();
        if (Strings.isNullOrEmpty(inputUserName)) {
            etUserName.setError("Can not be empty");
            return;
        }

        String inputEmail = etEmail.getText().toString();
        if (Strings.isNullOrEmpty(inputEmail)) {
            etEmail.setError("Can not be empty");
            return;
        }

        String password1 = etPassword.getText().toString();
        if (Strings.isNullOrEmpty(password1)) {
            etPassword.setError("Can not be empty");
            return;
        }

        String password2 = etRepeatPassword.getText().toString();
        if (Strings.isNullOrEmpty(password2)) {
            etRepeatPassword.setError("Can not be empty");
            return;
        }

        if (password1.equals(password2)) {
            DialogHelper.showProgressDialog(getActivity(), R.string.dialog_msg_login);
            mPresenter.signUpEmailAndPassWord(inputEmail, password1, inputUserName);
        } else {
            etRepeatPassword.setError("Password not matching");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onDestroyView() {
        Timber.v("onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void signUpSuccess(@NonNull FirebaseUser firebaseUser) {
        DialogHelper.dismissProgressDialog();
        Timber.d("Sign up as: %s", firebaseUser.getDisplayName());
        SignUpActivity activity = (SignUpActivity) getActivity();
        activity.signUpSuccess();
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
