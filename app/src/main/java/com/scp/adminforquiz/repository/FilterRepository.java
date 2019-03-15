package com.scp.adminforquiz.repository;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FilterRepository {

    @NotNull
    private MyPreferenceManager preferences;

    private BehaviorRelay<Boolean> userFilterAscendingTypeRelay;

    private BehaviorRelay<String> userSortFieldNameRelay;

    @Inject
    FilterRepository(@NotNull MyPreferenceManager preferences) {
        this.preferences = preferences;
        userFilterAscendingTypeRelay = BehaviorRelay.createDefault(this.preferences.getUserFilterAscending());
        userSortFieldNameRelay = BehaviorRelay.createDefault(this.preferences.getUserSortFieldName());
    }

    public Flowable<Boolean> getUserFilterAscendingType() {
        return userFilterAscendingTypeRelay.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void setUserFilterAscendingType(boolean userFilterAscendingType) {
        preferences.setUserFilterAscending(userFilterAscendingType);
        userFilterAscendingTypeRelay.accept(userFilterAscendingType);
    }

    public Flowable<String> getUserSortFieldName() {
        return userSortFieldNameRelay.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void setUserSortFieldName(String userSortFieldName) {
        preferences.setUserSortFieldName(userSortFieldName);
        userSortFieldNameRelay.accept(userSortFieldName);
    }
}
