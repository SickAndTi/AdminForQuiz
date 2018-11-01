package com.scp.adminforquiz.mvp;

import android.support.v7.widget.SwitchCompat;
import android.widget.RadioGroup;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.db.dao.QuizDao;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class FilterPresenter extends MvpPresenter<FilterView> {
    @Inject
    ApiClient apiClient;
    @Inject
    QuizConverter quizConverter;
    @Inject
    QuizDao quizDao;
    @Inject
    Router router;
    @Inject
    MyPreferenceManager preferences;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    BehaviorRelay<SwitchCompat> switchCompatBehaviorRelay = BehaviorRelay.create();
    BehaviorRelay<RadioGroup> radioGroupBehaviorRelay = BehaviorRelay.create();

    @Override

    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Observable.combineLatest(
                switchCompatBehaviorRelay,
                radioGroupBehaviorRelay,
                (isChecked, buttonId) ->
        ))
    }

    public Boolean isSwitchChecked() {
        return switchCompatBehaviorRelay.getValue().;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void filterById() {
        compositeDisposable.add(quizDao.getAll()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(quizzes -> {
                }));
    }

    public void filterByDateCreated() {
        compositeDisposable.add(quizDao.getAllByDateCreatedAsc()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(quizzes -> {
                }));
    }

    public void filterByDateUpdated() {
        compositeDisposable.add(quizDao.getAllByDateUpeatedAsc()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(quizzes -> {
                }));
    }

    public void filterByApproved() {
        compositeDisposable.add(quizDao.getAllByApprovedAsc()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(quizzes -> {
                }));
    }

    public void cancel() {
        router.backTo(Constants.ALL_QUIZ_SCREEN);

    }

    public void filter() {

    }
}
