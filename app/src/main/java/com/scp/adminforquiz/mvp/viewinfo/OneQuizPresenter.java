package com.scp.adminforquiz.mvp.viewinfo;

import android.content.Context;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.db.Repository;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import kotlin.Triple;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;
import toothpick.Toothpick;

@InjectViewState
public class OneQuizPresenter extends MvpPresenter<OneQuizView> {
    @Inject
    Repository repository;
    @Inject
    Context context;
    @Inject
    Router router;
    @Inject
    ApiClient apiClient;
    @Inject
    QuizConverter quizConverter;
    @Inject
    MyPreferenceManager preferences;
    private Long quizAuthorId;
    private Long quizTranslationAuthorId;
    private Long quizTranslationPhraseAuthorId;
    private Long quizId;
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
        Timber.d("User Id from pref : %s", preferences.getUserId());
        Timber.d("isAdmin from PREF : %s", preferences.getIsAdmin());
        compositeDisposable.add(Flowable.combineLatest(
                repository.getQuizByIdFlowable(quizId),
                repository.getTranslationsByIdFlowable(quizId),
                repository.getPhrasesByIdFlowable(quizId),
                (Function3<Quiz, List<QuizTranslation>, List<QuizTranslationPhrase>, Triple>) Triple::new
        )
                .map(o -> repository.getFullQuizById(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz -> {
                    quizAuthorId = quiz.authorId;
                    for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                        quizTranslationAuthorId = quizTranslation.authorId;
                        for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                            quizTranslationPhraseAuthorId = quizTranslationPhrase.authorId;
                        }
                    }
                    getViewState().showQuiz(quiz);
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void deleteQuiz() {
        if (quizAuthorId.equals(preferences.getUserId()) || preferences.getIsAdmin()) {
            compositeDisposable.add(apiClient.deleteNwQuizById(quizId)
                    .map(aBoolean -> repository.deleteQuiz(quizId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                    .doOnEvent((integer, throwable) -> getViewState().showProgressBar(false))
                    .subscribe(integer -> router.backTo(Constants.ALL_QUIZ_SCREEN),
                            error -> getViewState().showError(error.toString())
                    ));
        } else showNoAuthorities();
    }

    private void showNoAuthorities() {
        Toast.makeText(context, R.string.deleteToastText, Toast.LENGTH_LONG).show();
    }

    public void goToAddTranslationFragment() {
        router.navigateTo(Constants.ADD_TRANSLATION_SCREEN, quizId);
    }

    public void approveQuizById(Long quizId, boolean approve) {
        compositeDisposable.add(apiClient.approveNwQuizById(quizId, approve)
                .map(nwQuiz -> repository.insertQuiz(quizConverter.convert(nwQuiz)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    public void approveTranslationById(Long translationId, boolean approve) {
        compositeDisposable.add(apiClient.approveNwQuizTranslationById(translationId, approve)
                .map(nwQuizTranslation -> repository.insertTranslation(quizConverter.convertTranslation(nwQuizTranslation, quizId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }

    public void deleteTranslationById(Long translationId) {
        if (quizTranslationAuthorId.equals(preferences.getUserId()) || preferences.getIsAdmin()) {
            compositeDisposable.add(apiClient.deleteNwQuizTranslationById(translationId)
                    .map(aBoolean -> repository.deleteTranslation(translationId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                    .doOnEvent((integer, throwable) -> getViewState().showProgressBar(false))
                    .subscribe(integer -> {
                            }, error -> getViewState().showError(error.toString())
                    ));
        } else showNoAuthorities();
    }

    public void deletePhraseById(Long phraseId) {
        if (quizTranslationPhraseAuthorId.equals(preferences.getUserId()) || preferences.getIsAdmin()) {
            compositeDisposable.add(apiClient.deleteNwQuizTranslationPhraseById(phraseId)
                    .map(aBoolean -> repository.deletePhrase(phraseId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                    .doOnEvent((integer, throwable) -> getViewState().showProgressBar(false))
                    .subscribe(integer -> {
                            }, error -> getViewState().showError(error.toString())
                    ));
        } else showNoAuthorities();
    }

    public void goToAddPhraseFragment(Long quizTranslationId) {
        router.navigateTo(Constants.ADD_PHRASE_SCREEN, quizTranslationId);
    }

    public void approvePhraseById(Long phraseId, Long translationId, boolean approve) {
        compositeDisposable.add(apiClient.approveNwQuizTranslationPhraseById(phraseId, approve)
                .map(nwQuizTranslationPhrase -> repository.insertPhrase(quizConverter.convertTranslationPhrase(nwQuizTranslationPhrase, translationId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((aLong, throwable) -> getViewState().showProgressBar(false))
                .subscribe(aLong -> {
                        }, error -> getViewState().showError(error.toString())
                ));
    }
}
