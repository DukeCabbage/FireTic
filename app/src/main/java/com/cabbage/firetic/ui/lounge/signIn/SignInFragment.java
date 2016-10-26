package com.cabbage.firetic.ui.lounge.signIn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.Player;
import com.cabbage.firetic.ui.lounge.LoungeActivity;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class SignInFragment extends Fragment implements SignInContract.View {

    @BindView(R.id.tv_welcome) TextView tvWelcome;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.btn_connect) Button btnConnect;
    private Unbinder unbinder;
    private LoungeActivity activity;
    private SignInContract.Presenter mPresenter;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @OnClick(R.id.btn_connect)
    void connectOnClick(View v) {
        String inputEmail = etEmail.getText().toString();
        if (Strings.isNullOrEmpty(inputEmail)) {
            etEmail.setError("Can not be empty");
            return;
        }

        String inputPassword = etPassword.getText().toString();
        if (Strings.isNullOrEmpty(inputPassword)) {
            etPassword.setError("Can not be empty");
            return;
        }
        
        DialogHelper.showProgressDialog(activity, "Logging in...");
        mPresenter.loginEmailAndPassword(inputEmail, inputPassword);

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        activity = (LoungeActivity) getActivity();
        unbinder = ButterKnife.bind(this, rootView);

        final FirebaseRemoteConfig remoteConfig = MyApplication.component().fireConfig();
        remoteConfig.fetch()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Fetch Succeeded", Toast.LENGTH_LONG).show();
                            remoteConfig.activateFetched();
                        } else {
                            Toast.makeText(getActivity(), "Fetch Failed", Toast.LENGTH_LONG).show();
                        }
                        String welcomeMessage = remoteConfig.getString("welcome_message");
                        tvWelcome.setText(welcomeMessage);
                    }
                });

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
    public void signInSuccess(@NonNull Player player) {
        DialogHelper.dismissProgressDialog();
        if (getView() != null) {
            Snackbar.make(getView(), String.format("Welcome, %s", player.getUserName()), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void signInFail(@Nullable String errMsg) {
        DialogHelper.dismissProgressDialog();

        if (getView() != null) {
            errMsg = Strings.isNullOrEmpty(errMsg) ? "Sign in fail" : errMsg;
            Snackbar.make(getView(), errMsg, Snackbar.LENGTH_LONG).setDuration(10000).show();
        }
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
