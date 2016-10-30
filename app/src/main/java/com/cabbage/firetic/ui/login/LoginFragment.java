package com.cabbage.firetic.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.cabbage.firetic.ui.uiUtils.DialogHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.base.Strings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

public class LoginFragment extends Fragment
        implements LoginContract.View, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_FACEBOOK = CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();

    @BindView(R.id.tv_welcome) TextView tvWelcome;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.btn_connect) Button btnConnect;
    @BindView(R.id.sign_in_button) SignInButton signInButton;
    @BindView(R.id.login_button) LoginButton loginButton;
    private Unbinder unbinder;
//    private LoginActivity activity;
    private LoginContract.Presenter mPresenter;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;

    public static LoginFragment newInstance() {
        return new LoginFragment();
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

        DialogHelper.showProgressDialog(getActivity(), "Logging in...");
        mPresenter.loginEmailAndPassword(inputEmail, inputPassword);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @OnClick(R.id.sign_in_button)
    void googleSignIn(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
//        activity = (LoginActivity) getActivity();
        unbinder = ButterKnife.bind(this, rootView);

        final FirebaseRemoteConfig remoteConfig = MyApplication.component().fireConfig();
        remoteConfig.fetch()
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(), "Fetch Succeeded", Toast.LENGTH_SHORT).show();
                        remoteConfig.activateFetched();
                    } else {
                        Toast.makeText(getActivity(), "Fetch Failed", Toast.LENGTH_SHORT).show();
                    }
                    String welcomeMessage = remoteConfig.getString("welcome_message");
                    tvWelcome.setText(welcomeMessage);
                });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        
        LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.firebaseAuthWithFacebook(loginResult);
            }

            @Override
            public void onCancel() {
                Timber.w("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });

        loginButton.setOnClickListener(view -> Toast.makeText(getActivity(), "External click listener", Toast.LENGTH_SHORT).show());

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
    public void loginSuccess(@NonNull Player player) {
        DialogHelper.dismissProgressDialog();
        LoginActivity loginActivity = (LoginActivity) getActivity();
        loginActivity.signInSuccess();
    }

    @Override
    public void loginFail(@Nullable String errMsg) {
        DialogHelper.dismissProgressDialog();

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loginFail(connectionResult.getErrorMessage());
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.revoke_user:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        status -> Timber.d(status.getStatusMessage()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
