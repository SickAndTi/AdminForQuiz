package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.QuizTranslationPhrase;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import kotlin.Triple;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class EditPresenter extends MvpPresenter<EditView> {
    @Inject
    QuizDao quizDao;
    @Inject
    ApiClient apiClient;
    @Inject
    Router router;
    private Long quizId;
    @Inject
    QuizConverter quizConverter;
    @Inject
    MyPreferenceManager preferences;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Flowable.combineLatest(
                quizDao.getQuizByIdOrErrorWithUpdates(quizId),
                quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId),
                quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId),
                (Function3<Quiz, List<QuizTranslation>, List<QuizTranslationPhrase>, Triple>) Triple::new
        )
                .map(o ->
                        quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz ->
                        getViewState().showEditQuiz(quiz)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void addTranslationPhrase(Long nwQuizTranslationId, String translationPhrase) {
        preferences.setAccessToken(null);
        compositeDisposable.add(apiClient.addNwQuizTranslationPhrase(nwQuizTranslationId, translationPhrase)
                .map(nwQuizTranslation ->
                        quizDao.insertQuizTranslationWithPhrases(quizConverter.convertTranslation(nwQuizTranslation, quizId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable ->
                        getViewState().showProgress(true))
                .doOnEvent((aLong, throwable) ->
                        getViewState().showProgress(false))
                .subscribe(aLong -> {
                        }, error ->
                                getViewState().showError(error.toString())
                ));
    }

    public void addTranslation(String langCode, String translationText, String translationDescription) {
        compositeDisposable.add(apiClient.addNwQuizTranslation(quizId, langCode, translationText, translationDescription)
                .map(nwQuiz -> quizDao.insertQuizWithQuizTranslations(quizConverter.convert(nwQuiz)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgress(false))
                .subscribe(aLong -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    public void updateTranslationDescription(Long quizTranslationId, String description) {
        compositeDisposable.add(apiClient.updateNwQuizTranslationDescription(quizTranslationId, description)
                .map(nwQuizTranslation -> quizDao.insertQuizTranslation(quizConverter.convertTranslation(nwQuizTranslation, quizId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgress(false))
                .subscribe(aLong -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    public void deleteNwQuizById() {
        compositeDisposable.add(apiClient.deleteNwQuizById(quizId)
                .map(aBoolean -> quizDao.deleteQuizById(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnEvent((integer, throwable) -> getViewState().showProgress(false))
                .subscribe(integer -> backToAllQuizFragment(),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void deleteNwQuizTranslationById(Long nwQuizTranslationId) {
        compositeDisposable.add(apiClient.deleteNwQuizTranslationById(nwQuizTranslationId)
                .map(aBoolean -> quizDao.deleteQuizTranslationById(nwQuizTranslationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnEvent((integer, throwable) -> getViewState().showProgress(false))
                .subscribe(integer -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    public void deleteNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId) {
        compositeDisposable.add(apiClient.deleteNwQuizTranslationPhraseById(nwQuizTranslationPhraseId)
                .map(aBoolean -> quizDao.deleteQuizTranslationPhraseById(nwQuizTranslationPhraseId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnEvent((integer, throwable) -> getViewState().showProgress(false))
                .subscribe(integer -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    public void approveNwQuizById(Long nwQuizId, Boolean approve) {
        compositeDisposable.add(apiClient.approveNwQuizById(nwQuizId, approve)
                .map(nwQuiz -> quizDao.insert(quizConverter.convert(nwQuiz)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgress(false))
                .subscribe(aLong -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    private void backToAllQuizFragment() {
        router.backTo(Constants.ALL_QUIZ_SCREEN);
    }
}