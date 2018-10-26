package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        vkImage = view.findViewById(R.id.vkImage);
        googleImage = view.findViewById(R.id.googleImage);
        faceBookImage = view.findViewById(R.id.faceBookImage);
        authPagerAdapter = new AuthPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(authPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        String[] tabTitles = getResources().getStringArray(R.array.authTabTitles);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);//TODO inflate TextView with parameters into this
            TextView textView = new TextView(getContext());
            textView.setText(tabTitles[i]);
            tab.setCustomView(textView);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().setBackgroundResource(R.color.selectTabColor);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().setBackgroundResource(android.R.color.transparent);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getCustomView().setBackgroundResource(R.color.selectTabColor);
            }
        });
        tabLayout.getTabAt(0).select();
//
//        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener();
//        viewPager.addOnPageChangeListener(onPageChangeListener);
//        onPageChangeListener.onPageSelected(1);
    }
}
