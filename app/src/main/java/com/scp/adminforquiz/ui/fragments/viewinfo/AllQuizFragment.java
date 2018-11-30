package com.scp.adminforquiz.ui.fragments.viewinfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.mvp.viewinfo.AllQuizPresenter;
import com.scp.adminforquiz.mvp.viewinfo.AllQuizView;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.scp.adminforquiz.ui.adapters.AllQuizRecyclerViewAdapter;
import com.scp.adminforquiz.util.EndlessRecyclerViewScrollListener;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import toothpick.Toothpick;

public class AllQuizFragment extends MvpAppCompatFragment implements AllQuizView {
    @InjectPresenter
    AllQuizPresenter allQuizPresenter;
    @Inject
    MyPreferenceManager preferences;
    RecyclerView recyclerView;
    AllQuizRecyclerViewAdapter allQuizRecyclerViewAdapter;
    View progressBarAllQuiz;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    BottomSheetBehavior bottomSheetBehavior;
    View bottomSheet;
    SwitchCompat ascSwitch;
    RadioGroup radioGroup;
    Button btnOK, btnCancel;
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
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        ascSwitch = view.findViewById(R.id.ascSwitch);
        ascSwitch.setChecked(preferences.getUserFilterAscending());
        radioGroup = view.findViewById(R.id.radioGroup);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> filterQuizzes());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> outFromBottomSheet());
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.allquiz_menu);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.createQuiz:
                    allQuizPresenter.goToCreateQuizFragment();
                    break;
                case R.id.filter:
                    toggleBottomSheet();
                    break;
                case R.id.logout:
                    showLogoutDialog();
                    break;
            }
            return super.onOptionsItemSelected(menuItem);
        });

        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.icSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Timber.d("ON QUERY TEXT SUBMIT : %s", query);
                allQuizPresenter.onQueryTextChanged(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Timber.d("ON QUERY TEXT CHANGE : %s", query);
                allQuizPresenter.onQueryTextChanged(query);
                return true;
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBarAllQuiz = view.findViewById(R.id.flProgressBarAllQuiz);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        allQuizRecyclerViewAdapter = new AllQuizRecyclerViewAdapter(quiz -> allQuizPresenter.goToQuizFragment(quiz));
        recyclerView.setAdapter(allQuizRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> allQuizPresenter.loadQuizzesFromApi(1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void filterQuizzes() {
        if (ascSwitch.isChecked()) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case -1:
                    Toast.makeText(getContext(), R.string.chooseFilter, Toast.LENGTH_LONG).show();
                    break;
                case R.id.filterById:
                    allQuizPresenter.filterById();
                    break;
                case R.id.filterByDateCreated:
                    allQuizPresenter.filterByDateCreated();
                    break;
                case R.id.filterByDateUpdated:
                    allQuizPresenter.filterByDateUpdated();
                    break;
                case R.id.filterByApproved:
                    allQuizPresenter.filterByApproved();
                    break;
            }
        } else {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case -1:
                    Toast.makeText(getContext(), R.string.chooseFilter, Toast.LENGTH_LONG).show();
                    break;
                case R.id.filterById:
                    allQuizPresenter.filterByIdDesc();
                    break;
                case R.id.filterByDateCreated:
                    allQuizPresenter.filterByDateCreatedDesc();
                    break;
                case R.id.filterByDateUpdated:
                    allQuizPresenter.filterByDateUpdatedDesc();
                    break;
                case R.id.filterByApproved:
                    allQuizPresenter.filterByApprovedDesc();
                    break;
            }
        }
    }

    private void outFromBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAllQuiz.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    public void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void showLogoutDialog() {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void showQuizList(List<Quiz> quizList) {
        Timber.d("setQuizList : %s", quizList.size());
        allQuizRecyclerViewAdapter.setQuizList(quizList);
    }

    @Override
    public void filterQueryText(String queryText) {
        Timber.d("FILTER QUERY TEXT : %s", queryText);
        allQuizRecyclerViewAdapter.filter(queryText);
    }

    @Override
    public void showError(String errorMessage) {
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
                    allQuizPresenter.loadQuizzesFromApi(totalItemsCount / Constants.PAGE_SIZE + 1);
                }
            });
        }
    }

    @Override
    public void showBottomSheet(boolean showBottomSheet) {
        bottomSheetBehavior.setState(showBottomSheet ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_HIDDEN);
    }
}