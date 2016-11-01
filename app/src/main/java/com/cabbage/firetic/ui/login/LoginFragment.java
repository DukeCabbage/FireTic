package com.cabbage.firetic.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.ui.signup.SignUpActivity;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.common.base.Strings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class LoginFragment extends AbstractLoginFragment {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_FACEBOOK = CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();

    @BindView(R.id.tv_welcome) TextView tvWelcome;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.btn_facebook) LoginButton btnFacebook;

    private Unbinder unbinder;
    private LoginContract.Presenter mPresenter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @OnClick(R.id.btn_google)
    void googleSignIn() {
        DialogHelper.showProgressDialog(getActivity(), R.string.dialog_msg_login);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btn_facebook_proxy)
    void faceBookOnClick() {
        DialogHelper.showProgressDialog(getActivity(), R.string.dialog_msg_login);
        btnFacebook.performClick();
    }

    @OnClick(R.id.btn_sign_up)
    void signUpOnClick() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    void loginOnClick(View v) {
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

        DialogHelper.showProgressDialog(getActivity(), R.string.dialog_msg_login);
        mPresenter.loginEmailAndPassword(inputEmail, inputPassword);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        final FirebaseRemoteConfig remoteConfig = MyApplication.component().fireConfig();
        tvWelcome.setText(R.string.default_welcome);
        remoteConfig.fetch()
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) remoteConfig.activateFetched();
                    String welcomeMessage = remoteConfig.getString("welcome_message");
                    tvWelcome.setText(welcomeMessage);
                });

        setUpGoogleLogin();
        setUpFacebookLogin(btnFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.firebaseAuthWithFacebook(loginResult);
            }

            @Override
            public void onCancel() {
                loginFail("");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
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
    public void loginSuccess(@NonNull FirebaseUser firebaseUser) {
        DialogHelper.dismissProgressDialog();
        Timber.d("Sign in as: %s", firebaseUser.getDisplayName());
        LoginActivity loginActivity = (LoginActivity) getActivity();
        loginActivity.signInSuccess();
    }

    @Override
    public void loginFail(@Nullable String errMsg) {
        DialogHelper.dismissProgressDialog();
        revokeFacebook();
        revokeGoogle();

        if (getView() != null) {
            errMsg = Strings.isNullOrEmpty(errMsg) ? "Sign in fail" : errMsg;
            Snackbar.make(getView(), errMsg, Snackbar.LENGTH_LONG).setDuration(10000).show();
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == RC_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Timber.d("handleSignInResult: %s", result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mPresenter.firebaseAuthWithGoogle(acct);
        } else {
            loginFail(result.getStatus().getStatusMessage());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.login_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.revoke_google:
                revokeGoogle();
                return true;
            case R.id.revoke_facebook:
                revokeFacebook();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
