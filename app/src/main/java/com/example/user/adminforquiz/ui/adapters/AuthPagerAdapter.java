package com.example.user.adminforquiz.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.adminforquiz.ui.fragments.SignInFragment;
import com.example.user.adminforquiz.ui.fragments.SignUpFragment;

public class AuthPagerAdapter extends FragmentPagerAdapter {

    public AuthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SignInFragment.newInstance();
            case 1:
                return SignUpFragment.newInstance();
            default:
                throw new IllegalArgumentException("Unexpected position: " + position);
        }
    }
}