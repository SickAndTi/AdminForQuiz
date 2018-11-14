package com.scp.adminforquiz.ui.fragments.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scp.adminforquiz.CommonUserData;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.mvp.authorization.AuthPresenter;
import com.scp.adminforquiz.mvp.authorization.AuthView;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.scp.adminforquiz.ui.adapters.AuthPagerAdapter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;
import toothpick.Toothpick;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    private static final int REQUEST_CODE_GOOGLE = 23;
    @InjectPresenter
    AuthPresenter authPresenter;
    @Inject
    ApiClient apiClient;
    @Inject
    Router router;
    @Inject
    MyPreferenceManager preferences;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView vkImage, googleImage, faceBookImage;
    AuthPagerAdapter authPagerAdapter;
    TextView toolbarTitle;
    GoogleSignInOptions gso;
    CallbackManager callbackManager;
    GoogleApiClient googleApiClient;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        toolbar = view.findViewById(R.id.toolbar);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), connectionResult -> Timber.d("ERROR"))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        toolbarTitle = view.findViewById(R.id.toolbarTitle);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewpager);
        vkImage = view.findViewById(R.id.vkImage);
        vkImage.setOnClickListener(v ->
                VKSdk.login(getActivity(), VKScope.EMAIL)
        );
        googleImage = view.findViewById(R.id.googleImage);
        googleImage.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
        });
        faceBookImage = view.findViewById(R.id.faceBookImage);
        faceBookImage.setOnClickListener(v -> {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
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
                                            error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show())
                            );
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Timber.d("ON ERROR FB :%s", exception.toString());
                        }
                    });
        });
        authPagerAdapter = new AuthPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(authPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        String[] tabTitles = getResources().getStringArray(R.array.authTabTitles);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TextView textView = new TextView(getContext());
            textView.setText(tabTitles[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.black));
            tab.setCustomView(textView);
        }
    }

    @Override
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
                                .subscribe(tokenResponse -> router.navigateTo(Constants.ALL_QUIZ_SCREEN)));
                    }

                    @Override
                    public void onError(VKError error) {
                        showError(error.toString());
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

                    }
                });
            }

            @Override
            public void onError(VKError error) {
                Timber.d("Error ; %s", error.toString());
            }
        })) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                compositeDisposable.add(apiClient.loginSocial(Constants.GOOGLE, result.getSignInAccount().getIdToken())
                        .doOnSuccess(tokenResponse -> {
                            preferences.setAccessToken(tokenResponse.accessToken);
                            preferences.setRefreshToken(tokenResponse.refreshToken);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tokenResponse -> router.navigateTo(Constants.ALL_QUIZ_SCREEN)));
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
}