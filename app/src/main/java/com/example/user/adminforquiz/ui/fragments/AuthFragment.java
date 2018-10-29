package com.example.user.adminforquiz.ui.fragments;

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
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.mvp.AuthPresenter;
import com.example.user.adminforquiz.mvp.AuthView;
import com.example.user.adminforquiz.ui.adapters.AuthPagerAdapter;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    @InjectPresenter
    AuthPresenter authPresenter;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView vkImage, googleImage, faceBookImage;
    AuthPagerAdapter authPagerAdapter;
    TextView toolbarTitle;

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
        toolbarTitle = view.findViewById(R.id.toolbarTitle);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        vkImage = view.findViewById(R.id.vkImage);
        vkImage.setOnClickListener(v -> authPresenter.regViaVk());
        googleImage = view.findViewById(R.id.googleImage);
        googleImage.setOnClickListener(v -> authPresenter.regViaGoogle());
        faceBookImage = view.findViewById(R.id.faceBookImage);
        faceBookImage.setOnClickListener(v -> authPresenter.regViaFacebook());
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
}
