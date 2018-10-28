package com.example.user.adminforquiz.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.mvp.AllQuizPresenter;
import com.example.user.adminforquiz.mvp.AllQuizView;
import com.example.user.adminforquiz.ui.adapters.AllQuizRecyclerViewAdapter;
import com.example.user.adminforquiz.util.EndlessRecyclerViewScrollListener;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class AllQuizFragment extends MvpAppCompatFragment implements AllQuizView {
    @InjectPresenter
    AllQuizPresenter allQuizPresenter;
    RecyclerView recyclerView;
    AllQuizRecyclerViewAdapter allQuizRecyclerViewAdapter;
    View progressBarAllQuiz;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static AllQuizFragment newInstance() {
        return new AllQuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_allquiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.allquiz_menu);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.createQuiz:
                    allQuizPresenter.goToCreateQuizFragment();
                    break;

                case R.id.logout:
                    LayoutInflater inflaterLogout = LayoutInflater.from(getContext());
                    @SuppressLint("InflateParams") View viewLogout = inflaterLogout.inflate(R.layout.dialog_logout, null);
                    AlertDialog.Builder mDialogBuilderLogout = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    mDialogBuilderLogout.setView(viewLogout);
                    mDialogBuilderLogout
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    (dialog, id) -> {
                                        allQuizPresenter.logout();
                                        dialog.cancel();
                                    })
                            .setNegativeButton("Cancel",
                                    (dialog, id) -> dialog.cancel());
                    AlertDialog alertDialogLogout = mDialogBuilderLogout.create();
                    alertDialogLogout.show();
                    break;
            }
            return super.onOptionsItemSelected(menuItem);
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBarAllQuiz = view.findViewById(R.id.flProgressBarAllQuiz);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        allQuizRecyclerViewAdapter = new AllQuizRecyclerViewAdapter(quiz -> allQuizPresenter.goToQuizFragment(quiz));
        recyclerView.setAdapter(allQuizRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> allQuizPresenter.setQuizzesFromDb());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAllQuiz.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void showQuizList(List<Quiz> quizList) {
        allQuizRecyclerViewAdapter.setQuizList(quizList);
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

    @Override
    public void showBottomProgress(boolean showBottomProgress) {
        ((AllQuizRecyclerViewAdapter) Objects.requireNonNull(recyclerView.getAdapter())).showBottomProgress(showBottomProgress);
    }

    @Override
    public void enableScrollListner(boolean enableScrollListener) {
        recyclerView.clearOnScrollListeners();
        if (enableScrollListener) {
            recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    allQuizPresenter.loadQuizzesFromPage(totalItemsCount / Constants.PAGE_SIZE + 1);
                }
            });
        }
    }
}