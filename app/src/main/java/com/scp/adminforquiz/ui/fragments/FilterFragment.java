package com.scp.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.FilterPresenter;
import com.scp.adminforquiz.mvp.FilterView;

import io.reactivex.disposables.CompositeDisposable;

public class FilterFragment extends MvpAppCompatFragment implements FilterView {
    @InjectPresenter
    FilterPresenter filterPresenter;
    Switch ascSwitch;
    RadioGroup radioGroup;
    RadioButton filterById, filterByDateCreated, filterByDateUpdated, filterByApproved;
    Button btnOK, btnCancel;
    View flProgressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static Fragment newInstance() {
        return new FilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ascSwitch = view.findViewById(R.id.ascSwitch);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> filterPresenter.filter());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> filterPresenter.cancel());
        radioGroup = view.findViewById(R.id.radioGroup);
        filterById = view.findViewById(R.id.filterById);
        filterByDateCreated = view.findViewById(R.id.filterByDateCreated);
        filterByDateUpdated = view.findViewById(R.id.filterByDateUpdated);
        filterByApproved = view.findViewById(R.id.filterByApproved);
        flProgressBar = view.findViewById(R.id.flProgressBar);
        compositeDisposable.add(RxCompoundButton.checkedChanges(ascSwitch)
                .subscribe(isChecked -> filterPresenter.getViewState().isChecked(isChecked)));
        compositeDisposable.add(RxCompoundButton.checkedChanges(filterById)
                .subscribe(aBoolean -> filterPresenter.getViewState().isChecked(aBoolean)));
        compositeDisposable.add(RxCompoundButton.checkedChanges(filterByDateCreated)
                .subscribe(aBoolean -> filterPresenter.getViewState().isChecked(aBoolean)));
        compositeDisposable.add(RxCompoundButton.checkedChanges(filterByDateUpdated)
                .subscribe(aBoolean -> filterPresenter.getViewState().isChecked(aBoolean)));
        compositeDisposable.add(RxCompoundButton.checkedChanges(filterByApproved)
                .subscribe(aBoolean -> filterPresenter.getViewState().isChecked(aBoolean)));
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioGroup.clearCheck();
            switch (checkedId) {
                case -1:
                    Toast.makeText(getContext(), R.string.nothingSelected, Toast.LENGTH_LONG).show();
                    break;
                case R.id.filterById:
                    break;
                case R.id.filterByDateCreated:

                    break;
                case R.id.filterByDateUpdated:

                    break;
                case R.id.filterByApproved:

                    break;
                default:
                    throw new IllegalArgumentException("Unexpected button");
            }
        });
    }

    @Override
    public void showProgress(boolean showProgress) {
        flProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void isChecked(View view, boolean isChecked) {
        view.check
    }
}
