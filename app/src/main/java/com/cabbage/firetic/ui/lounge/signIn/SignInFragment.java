package com.cabbage.firetic.ui.lounge.signIn;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.User;
import com.cabbage.firetic.ui.lounge.LoungeActivity;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    @BindView(R.id.btn_connect) Button btnConnect;
    private Unbinder unbinder;
    private LoungeActivity activity;
    private SignInContract.Presenter mPresenter;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @OnClick(R.id.btn_connect)
    void connectOnClick() {
        String inputUserName = etUsername.getText().toString();
        boolean validUserName = !TextUtils.isEmpty(inputUserName);

        if (validUserName) {
            DialogHelper.showProgressDialog(activity,"Logging in...");
            mPresenter.signInAs(inputUserName);
        } else {
            etUsername.setError("User name can't be empty");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        activity = (LoungeActivity) getActivity();
        unbinder = ButterKnife.bind(this, rootView);

        final FirebaseRemoteConfig remoteConfig = MyApplication.component().fireConfig();
        remoteConfig.fetch(60)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Fetch Succeeded",
                                    Toast.LENGTH_LONG).show();

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            remoteConfig.activateFetched();
                        } else {
                            Toast.makeText(getActivity(), "Fetch Failed",
                                    Toast.LENGTH_LONG).show();
                        }

                        String welcomeMessage = remoteConfig.getString("welcome_message");
                        tvWelcome.setText(welcomeMessage);
                    }
                });

        FirebaseDatabase database = MyApplication.component().fireDB();
        if (database == null) Timber.e("DB null");
        FirebaseAnalytics analytics = MyApplication.component().fireAnalytics();
        if (analytics == null) Timber.e("analytics null");

        return rootView;
    }

    @Override
    public void onResume() {
        Timber.v("onResume");
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        Timber.v("onPause");
        super.onPause();
        mPresenter.end();
    }

    @Override
    public void onDestroyView() {
        Timber.v("onDestroyView");
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void signInSuccess(@NonNull User user) {
        Timber.w("TODO: Sign in success");
        DialogHelper.dismissProgressDialog();

        if (getView() != null) {
            Snackbar.make(getView(), String.format("Welcome, %s", user.getUserName()), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void signInFail(@Nullable String errMsg) {
        Timber.w("TODO: Sign in fail");
        DialogHelper.dismissProgressDialog();
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
