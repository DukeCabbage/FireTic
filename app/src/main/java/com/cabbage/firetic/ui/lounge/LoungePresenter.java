//package com.cabbage.firetic.ui.lounge;
//
//import android.support.annotation.NonNull;
//
//import com.cabbage.firetic.model.DataManager;
//import com.cabbage.firetic.model.Player;
//
//import rx.Subscription;
//import rx.functions.Action1;
//import timber.log.Timber;
//
//public abstract class LoungePresenter {
//
//    protected LoungeMVPView mView;
//    protected DataManager mDataManager;
//    protected Subscription signInSubscription;
//
//    public LoungePresenter(@NonNull DataManager dataManager) {
//        this.mDataManager = dataManager;
//    }
//
//    void onStartView(@NonNull LoungeMVPView view) {
//        this.mView = view;
//    }
//
//    void onStopView() {
//        mView = null;
//        if (signInSubscription != null && !signInSubscription.isUnsubscribed()) {
//            signInSubscription.unsubscribe();
//        }
//    }
//
//    abstract void login(String inputUserName);
//
//    void revokeLogin() {
//        mDataManager.revokeActiveUser();
//        mView.logout();
//    }
//
//    protected Action1<Player> signInSuccessPipe() {
//        return new Action1<Player>() {
//            @Override
//            public void call(Player user) {
//                Timber.d(user.getUserName());
//                mView.loginSuccess(user);
//            }
//        };
//    }
//
//    protected Action1<Throwable> signInFailPipe() {
//        return new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                Timber.e(throwable.getLocalizedMessage());
//                mView.loginFailed(throwable.getLocalizedMessage());
//            }
//        };
//    }
//
//    /**-------------------------------------------------------------------------------------*/
//
//    abstract void getGames();
//
//    abstract void createGame();
//
//    abstract void deleteGame(String gameId, int position);
//
////    abstract void enterGame(String gameId, int position);
//}
