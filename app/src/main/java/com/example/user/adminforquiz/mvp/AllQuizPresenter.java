package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.api.ApiClient;

import javax.inject.Inject;

@InjectViewState
public class AllQuizPresenter extends MvpPresenter<AllQuizView> {
    @Inject
    ApiClient apiClient;



}
