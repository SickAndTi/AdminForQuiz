package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class AddPhrasePresenter extends MvpPresenter<AddPhraseView> {
    @Inject
    ApiClient apiClient;
    @Inject
    QuizDao quizDao;
    @Inject
    Router router;
    @Inject
    QuizConverter quizConverter;
    private Long quizTranslationId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BehaviorRelay<String> phraseRelay = BehaviorRelay.create();

//    public Long getQuizId() {
//        return quizId;
//    }
//
//    public void setQuizId(Long quizId) {
//        this.quizId = quizId;
//    }

    public Long getQuizTranslationId() {
        return quizTranslationId;
    }

    public void setQuizTranslationId(Long quizTranslationId) {
        this.quizTranslationId = quizTranslationId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    public void addPhrase() {
        compositeDisposable.add(apiClient.addNwQuizTranslationPhrase(quizTranslationId, phraseRelay.getValue())
                .map(nwQuizTranslation -> quizDao.insertQuizTranslationWithPhrases(quizConverter.convertTranslation(nwQuizTranslation, quizTranslationId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> router.navigateTo(Constants.EDIT_SCREEN)//quizId)
                        , error -> getViewState().showError(error.toString())
                ));
    }

    public void cancel() {
        router.backTo(Constants.EDIT_SCREEN);
    }

    public void onPhraseChanged(String phrase) {
        phraseRelay.accept(phrase);
        getViewState().enableButton(!TextUtils.isEmpty(phraseRelay.getValue()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
