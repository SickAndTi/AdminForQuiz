package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.mvp.AllQuizPresenter;
import com.example.user.adminforquiz.mvp.AllQuizView;
import com.example.user.adminforquiz.ui.adapters.RecyclerViewAdapter;

import java.util.List;

import timber.log.Timber;

public class AllQuizFragment extends MvpAppCompatFragment implements AllQuizView {
    @InjectPresenter
    AllQuizPresenter allQuizPresenter;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allquiz, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(() -> allQuizPresenter.loadDataFromApi());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNwQuizList(List<NwQuiz> nwQuizList) {
        if (recyclerView.getAdapter() == null) {
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();
            recyclerViewAdapter.setNwQuizList(nwQuizList);
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
            adapter.setNwQuizList(nwQuizList);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSwipeRefresherBar(boolean showSwipeRefresherBar) {
        swipeRefreshLayout.setRefreshing(showSwipeRefresherBar);
    }

}
