package com.example.user.adminforquiz.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.mvp.AllQuizPresenter;
import com.example.user.adminforquiz.mvp.AllQuizView;
import com.example.user.adminforquiz.ui.adapters.AllQuizRecyclerViewAdapter;

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
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static AllQuizFragment newInstance() {
        return new AllQuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allquiz, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBarAllQuiz = view.findViewById(R.id.flProgressBarAllQuiz);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        allQuizRecyclerViewAdapter = new AllQuizRecyclerViewAdapter(quiz -> allQuizPresenter.goToQuizFragment(quiz));
        recyclerView.setAdapter(allQuizRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(() -> allQuizPresenter.loadDataFromApiAndGetFromDb());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createQuiz:
                LayoutInflater inflater = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_create_quiz, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                mDialogBuilder.setView(view);
                final EditText etEnterScpNumber = view.findViewById(R.id.etEnterScpNumber);
                final EditText etEnterImageUrl = view.findViewById(R.id.etEnterImageUrl);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                (dialog, id) -> {
                                    allQuizPresenter.createNwQuiz(etEnterScpNumber.getText().toString(), etEnterImageUrl.getText().toString());
                                    dialog.cancel();
                                })
                        .setNegativeButton("Cancel",
                                (dialog, id) -> dialog.cancel());

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s) && s.toString().startsWith("http")) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                };
                etEnterImageUrl.addTextChangedListener(watcher);
                break;

            case R.id.logout:
                LayoutInflater inflaterLogout = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams")
                View viewLogout = inflaterLogout.inflate(R.layout.dialog_logout, null);
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
        return super.
                onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.allquiz_menu, menu);
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAllQuiz.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
}