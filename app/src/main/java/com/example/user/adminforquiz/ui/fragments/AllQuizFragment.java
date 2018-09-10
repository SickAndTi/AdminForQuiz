package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.mvp.AllQuizPresenter;
import com.example.user.adminforquiz.mvp.AllQuizView;
import com.example.user.adminforquiz.ui.adapters.RecyclerViewAdapter;

import javax.inject.Inject;

public class AllQuizFragment extends MvpAppCompatFragment implements AllQuizView {
    @InjectPresenter
    AllQuizPresenter allQuizPresenter;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ApiClient apiClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allquiz, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiClient.getNwQuizList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
