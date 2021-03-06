package com.scp.adminforquiz.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.scp.adminforquiz.ui.fragments.authorization.SignInFragment;
import com.scp.adminforquiz.ui.fragments.authorization.SignUpFragment;

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