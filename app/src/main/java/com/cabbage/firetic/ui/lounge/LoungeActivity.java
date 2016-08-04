package com.cabbage.firetic.ui.lounge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cabbage.firetic.R;
import com.cabbage.firetic.dagger.MyApplication;
import com.cabbage.firetic.model.DataManager;
import com.cabbage.firetic.model.User;
import com.cabbage.firetic.ui.uiUtils.DialogHelper;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class LoungeActivity extends AppCompatActivity {


    @BindView(R.id.toolbar) Toolbar mToolbar;

    DataManager mDataManager;
    Subscription signInSubscription;

    //region Actions
//    @SuppressWarnings("unused")
//    @OnClick(R.id.btn_connect)
//    void connectOnClick(View view) {
//
//        final String inputUserName = etUsername.getText().toString();
//        if (TextUtils.isEmpty(inputUserName)) {
//            return;
//        }
//
//        if (this.getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//
//        DialogHelper.showProgressDialog(this, "Logging in");
//        Observable<User> ob = mDataManager.signInAs(inputUserName);
//        signInSubscription = ob.subscribe(new Action1<User>() {
//            @Override
//            public void call(User user) {
//                Timber.d(user.getUserName());
//                DialogHelper.dismissProgressDialog();
//                checkIfSignedIn();
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                DialogHelper.dismissProgressDialog();
//                Toast.makeText(LoungeActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lounge);
        ButterKnife.bind(this);
        mDataManager = MyApplication.component().getDataManager();
        setUpAppBar();

        if (savedInstanceState == null) {
            checkIfSignedIn();
        }
    }

    private void setUpAppBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            throw new RuntimeException("Can not find toolbar");
        }
    }

    private void checkIfSignedIn() {
        User activeUser = mDataManager.getActiveUser();
        if (activeUser != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, LoungeFragment.newInstance(), "loungeFragment")
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, SignInFragment.newInstance(), "signInFragment")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lounge_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.revoke_user:
                mDataManager.revokeActiveUser();
                checkIfSignedIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        checkIfSignedIn();
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (signInSubscription != null && !signInSubscription.isUnsubscribed())
            signInSubscription.unsubscribe();

        DialogHelper.dismissProgressDialog();
    }

    void mockLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, LoungeFragment.newInstance(), "loungeFragment")
                .commit();
    }
}
