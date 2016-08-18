//package com.cabbage.firetic.ui.lounge;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.cabbage.firetic.R;
//import com.cabbage.firetic.dagger.MyApplication;
//import com.cabbage.firetic.model.DataManager;
//import com.cabbage.firetic.model.User;
//import com.cabbage.firetic.ui.base.BaseActivity;
//import com.cabbage.firetic.ui.lounge.signIn.SignInFragment;
//import com.cabbage.firetic.ui.uiUtils.DialogHelper;
//import com.google.firebase.crash.FirebaseCrash;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import timber.log.Timber;
//
//public class LoungeActivity extends BaseActivity implements LoungeMVPView {
//
//    DataManager mDataManager;
//    @Inject LoungePresenter loungePresenter;
//
//    @BindView(R.id.toolbar) Toolbar mToolbar;
//
//    SignInFragment signInFragment;
//    LoungeFragment loungeFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lounge);
//        ButterKnife.bind(this);
//        setUpAppBar();
//
//        mDataManager = MyApplication.component().getDataManager();
//        mActivityComponent.inject(this);
//        if (loungePresenter == null) {
//            throw new RuntimeException("Presenter null");
//        }
//
//        signInFragment = SignInFragment.newInstance();
//        loungeFragment = LoungeFragment.newInstance();
//
//        checkIfSignedIn();
//
//        FirebaseCrash.log("Activity created");
//        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
//    }
//
//    private void setUpAppBar() {
//        setSupportActionBar(mToolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(R.string.app_name);
//        } else {
//            throw new RuntimeException("Can not find toolbar");
//        }
//    }
//
//    private void checkIfSignedIn() {
//        User activeUser = mDataManager.getActiveUser();
//        if (activeUser != null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.content, loungeFragment, "loungeFragment")
//                    .commit();
//        } else {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.content, signInFragment, "signInFragment")
//                    .commit();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.lounge_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.revoke_user:
//                loungePresenter.revokeLogin();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        loungePresenter.onStartView(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        loungePresenter.onStopView();
//        DialogHelper.dismissProgressDialog();
//    }
//
//    /**-----------------------------------------------------------------------------------*/
//    @Override
//    public void loginSuccess(@NonNull User user) {
//        String message = String.format("Login as: %s", user.getUserName());
//        Timber.i(message);
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//        DialogHelper.dismissProgressDialog();
//
//        checkIfSignedIn();
//    }
//
//    @Override
//    public void loginFailed(@Nullable String message) {
//        String errorMessage = message == null ? "Login failed" : message;
//        Timber.e(errorMessage);
//        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//        DialogHelper.dismissProgressDialog();
//    }
//
//    @Override
//    public void logout() {
//        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
//        checkIfSignedIn();
//    }
//
//    /**-----------------------------------------------------------------------------------*/
//
//    @Override
//    public void updateGameList(List<String> games) {
//
//    }
//
//    @Override
//    public void updateGame(String gameId) {
//
//    }
//}
