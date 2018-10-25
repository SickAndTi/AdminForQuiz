package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.jakewharton.rxrelay2.BehaviorRelay;
import java.util.Arrays;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class AddTranslationPresenter extends MvpPresenter<AddTranslationView> {
    @Inject
    Router router;
    @Inject
    QuizDao quizDao;
    @Inject
    ApiClient apiClient;
    @Inject
    QuizConverter quizConverter;
    private Long quizId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BehaviorRelay<String> langCodeRelay = BehaviorRelay.create();
    private BehaviorRelay<String> titleRelay = BehaviorRelay.create();
    private BehaviorRelay<String> descriptionRelay = BehaviorRelay.create();
    private String[] locales = Locale.getISOLanguages();

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Observable.combineLatest(
                langCodeRelay,
                titleRelay,
                descriptionRelay,
                (langCodeText, titleText, descriptionText) -> !TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descriptionText) && Arrays.asList(locales).contains(langCodeText))
                .subscribe(isValid -> getViewState().enableButton(isValid))
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void cancel() {
        router.backTo(Constants.EDIT_SCREEN);
    }

    public void addTranslation() {
        compositeDisposable.add(apiClient.addNwQuizTranslation(quizId, langCodeRelay.getValue(), titleRelay.getValue(), descriptionRelay.getValue())
                .map(nwQuiz -> quizDao.insertQuizWithQuizTranslations(quizConverter.convert(nwQuiz)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> router.navigateTo(Constants.EDIT_SCREEN, quizId),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void onLangCodeChanged(String langCode) {
        langCodeRelay.accept(langCode);
    }

    public void onTitleChanged(String title) {
        titleRelay.accept(title);
    }

    public void onDescriptionChanged(String description) {
        descriptionRelay.accept(description);
    }
}
