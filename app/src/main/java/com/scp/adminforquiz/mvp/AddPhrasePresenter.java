package com.scp.adminforquiz.mvp;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.db.dao.QuizDao;

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
    private String phraseText;

    public void setQuizTranslationId(Long quizTranslationId) {
        this.quizTranslationId = quizTranslationId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    public void addPhrase() {
        compositeDisposable.add(apiClient.addNwQuizTranslationPhrase(quizTranslationId, phraseText)
                .map(nwQuizTranslation -> quizDao.insertQuizTranslationWithPhrases(quizConverter.convertTranslation(nwQuizTranslation, quizDao.getQuizIdByQuizTranslationId(quizTranslationId))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> router.backTo(Constants.EDIT_SCREEN),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void cancel() {
        router.backTo(Constants.EDIT_SCREEN);
    }

    public void onPhraseChanged(String phrase) {
        phraseText = phrase;
        getViewState().enableButton(!TextUtils.isEmpty(phraseText));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
