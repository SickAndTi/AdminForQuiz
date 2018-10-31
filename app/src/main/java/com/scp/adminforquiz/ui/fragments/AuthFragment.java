package com.scp.adminforquiz.ui.fragments;

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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.AuthPresenter;
import com.scp.adminforquiz.mvp.AuthView;
import com.scp.adminforquiz.ui.adapters.AuthPagerAdapter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import timber.log.Timber;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    private static final int RC_SIGN_IN = 23;
    @InjectPresenter
    AuthPresenter authPresenter;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView vkImage, googleImage, faceBookImage;
    AuthPagerAdapter authPagerAdapter;
    TextView toolbarTitle;
    GoogleSignInOptions gso;
    CallbackManager callbackManager;
//    LoginButton loginButton;

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
        toolbar = view.findViewById(R.id.toolbar);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), connectionResult -> Timber.d("ERROR"))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        toolbarTitle = view.findViewById(R.id.toolbarTitle);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        vkImage = view.findViewById(R.id.vkImage);
//        loginButton = view.findViewById(R.id.login_button);
//        loginButton.setFragment(this);
        vkImage.setOnClickListener(v -> {
            authPresenter.regViaVk();
            VKSdk.login(getActivity(), VKScope.EMAIL);

        });
        googleImage = view.findViewById(R.id.googleImage);
        googleImage.setOnClickListener(v -> {
            authPresenter.regViaGoogle();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);

        });
        faceBookImage = view.findViewById(R.id.faceBookImage);
        faceBookImage.setOnClickListener(v -> {
            authPresenter.regViaFacebook();
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Timber.d("LOGIN FB RESULT : %s", loginResult.getAccessToken().toString());
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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Timber.d("RESULT:%s", result.getSignInAccount().getEmail());
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Timber.d("RESULT : %s", res.accessToken);
                VKRequest request = VKApi.users().get();
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        Timber.d("VK RESPONSE :%s", response.toString());
//TODO make User Class to request on Serever
                    }

                    @Override
                    public void onError(VKError error) {
                        Timber.d("VKERROR : %s", error.toString());
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
        })) ;
        super.onActivityResult(requestCode, resultCode, data);
    }
}

