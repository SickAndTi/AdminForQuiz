package com.scp.adminforquiz.mvp.authorization;

import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.model.CommonUserData;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;
import toothpick.Toothpick;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> {
    @Inject
    ApiClient apiClient;
    @Inject
    Router router;
    @Inject
    MyPreferenceManager preferences;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int REQUEST_CODE_GOOGLE = 23;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        compositeDisposable.add(apiClient.loginSocial(Constants.FACEBOOK, loginResult.getAccessToken().getToken())
                                .doOnSuccess(tokenResponse -> {
                                    preferences.setAccessToken(tokenResponse.accessToken);
                                    preferences.setRefreshToken(tokenResponse.refreshToken);
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(tokenResponse -> router.navigateTo(Constants.ALL_QUIZ_SCREEN),
                                        error -> getViewState().showError(error.toString())
                                ));
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Timber.d("ON ERROR FB :%s", exception.toString());
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken vkAccessToken) {
                CommonUserData commonUserData = new CommonUserData();
                commonUserData.email = vkAccessToken.email;
                commonUserData.id = vkAccessToken.userId;
                VKRequest request = VKApi.users().get();
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUserFull user = ((VKList<VKApiUserFull>) response.parsedModel).get(0);
                        commonUserData.firstName = user.first_name;
                        commonUserData.lastName = user.last_name;
                        commonUserData.avatarUrl = user.photo_200;
                        commonUserData.fullName = user.first_name + "" + user.last_name;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        compositeDisposable.add(apiClient.loginSocial(Constants.VK, gson.toJson(commonUserData))
                                .doOnSuccess(tokenResponse -> {
                                    preferences.setAccessToken(tokenResponse.accessToken);
                                    preferences.setRefreshToken(tokenResponse.refreshToken);

                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(tokenResponse -> router.navigateTo(Constants.ALL_QUIZ_SCREEN),
                                        error -> getViewState().showError(error.toString()))
                        );
                    }

                    @Override
                    public void onError(VKError error) {
                        getViewState().showError(error.toString());
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

                    }
                });
            }

            @Override
            public void onError(VKError error) {
                Timber.d("Error ; %s", error.toString());
                getViewState().showError(error.toString());
            }
        })) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    compositeDisposable.add(apiClient.loginSocial(Constants.GOOGLE, result.getSignInAccount().getIdToken())
                            .doOnSuccess(tokenResponse -> {
                                preferences.setAccessToken(tokenResponse.accessToken);
                                preferences.setRefreshToken(tokenResponse.refreshToken);
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(tokenResponse -> router.navigateTo(Constants.ALL_QUIZ_SCREEN)));
                } else Timber.d("ERROR : %s", result.getStatus());
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
