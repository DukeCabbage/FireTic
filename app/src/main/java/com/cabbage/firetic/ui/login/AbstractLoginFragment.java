package com.cabbage.firetic.ui.login;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.cabbage.firetic.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import timber.log.Timber;


public abstract class AbstractLoginFragment extends Fragment
        implements LoginContract.View, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    protected CallbackManager callbackManager;

    protected void setUpGoogleLogin() {
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
    }

    protected void revokeGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                status -> Timber.d(status.getStatusMessage())
        );
    }

    protected void setUpFacebookLogin(final LoginButton loginButton, final FacebookCallback<LoginResult> callback) {
        revokeFacebook();

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

    protected void revokeFacebook() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loginFail(connectionResult.getErrorMessage());
    }
}
