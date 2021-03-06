package com.scp.adminforquiz.mvp.edit;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.db.QuizRepository;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.api.NwQuiz;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class CreateQuizPresenter extends MvpPresenter<CreateQuizView> {

    @Inject
    Router router;
    @Inject
    ApiClient apiClient;
    @Inject
    QuizRepository quizRepository;
    @Inject
    QuizConverter quizConverter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BehaviorRelay<String> numberRelay = BehaviorRelay.create();
    private BehaviorRelay<String> imageRelay = BehaviorRelay.create();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Observable.combineLatest(
                numberRelay,
                imageRelay,
                (numberText, imageText) -> !TextUtils.isEmpty(numberText) && imageText.startsWith("http")
        )
                .subscribe(isValid -> {
                    getViewState().enableButton(isValid);
                    getViewState().setColorEnableButton(isValid);
                }));
    }

    public void onNumberChanged(String scpNumber) {
        numberRelay.accept(scpNumber);
    }

    public void onImageChanged(String imageUrl) {
        imageRelay.accept(imageUrl);
    }

    public void cancel() {
        router.backTo(Constants.ALL_QUIZ_SCREEN);
    }

    public void createQuiz() {
        NwQuiz nwQuiz = new NwQuiz();
        nwQuiz.scpNumber = numberRelay.getValue();
        nwQuiz.imageUrl = imageRelay.getValue();
        compositeDisposable.add(apiClient.createNwQuiz(nwQuiz)
                .toFlowable()
                .map(nwQuiz1 -> quizConverter.convert(nwQuiz1))
                .map(quiz -> quizRepository.insertQuiz(quiz))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> getViewState().showProgressBar(true))
                .doOnEach((notification) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> router.newRootScreen(Constants.ALL_QUIZ_SCREEN),
                        error -> getViewState().showError(error.toString())
                ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}