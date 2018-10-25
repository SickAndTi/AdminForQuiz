package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class UpdateTranslationDescriptionPresenter extends MvpPresenter<UpdateTranslationDescriptionView> {
    @Inject
    QuizDao quizDao;
    @Inject
    ApiClient apiClient;
    @Inject
    Router router;
    @Inject
    QuizConverter quizConverter;
    private Long quizTranslationId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String descriptionText;

    public void setQuizTranslationId(Long quizTranslationId) {
        this.quizTranslationId = quizTranslationId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(
                quizDao.getQuizTranslationDescriptionByQuizTranslationId(quizTranslationId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                        .doOnEvent((s, throwable) -> getViewState().showProgressBar(false))
                        .subscribe(s -> getViewState().setTranslationDescription(s),
                                error -> getViewState().showError(error.toString())
                        ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void onDescriptionChanged(String description) {
        descriptionText = description;
        getViewState().enableButton(!TextUtils.isEmpty(descriptionText));
    }

    public void cancel() {
        router.backTo(Constants.EDIT_SCREEN);
    }

    public void updateDescription() {
        compositeDisposable.add(apiClient.updateNwQuizTranslationDescription(quizTranslationId, descriptionText)
                .map(nwQuizTranslation -> quizDao.insertQuizTranslation(quizConverter.convertTranslation(nwQuizTranslation, quizDao.getQuizIdByQuizTranslationId(quizTranslationId))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> router.backTo(Constants.EDIT_SCREEN),
                        error -> getViewState().showError(error.toString())
                ));
    }
}
