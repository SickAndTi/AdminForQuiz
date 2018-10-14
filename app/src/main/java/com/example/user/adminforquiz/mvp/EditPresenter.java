package com.example.user.adminforquiz.mvp;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.QuizTranslationPhrase;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import kotlin.Triple;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;
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

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("CheckResult")
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Timber.d("onFirstViewAttach EditPresenter");
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Flowable.combineLatest(
                quizDao.getQuizByIdOrErrorWithUpdates(quizId),
                quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId),
                quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId),
                (Function3<Quiz, List<QuizTranslation>, List<QuizTranslationPhrase>, Triple>) (first, second, third) -> new Triple<Quiz, List<QuizTranslation>, List<QuizTranslationPhrase>>(first, second, third)
        )
                .map(o -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz -> getViewState().showEditQuiz(quiz)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public Disposable addTranslationPhrase(Long nwQuizTranslationId, String translationPhrase) {
        return apiClient.addNwQuizTranslationPhrase(nwQuizTranslationId, translationPhrase)
                .map(nwQuizTranslation -> quizDao.insertQuizTranslationWithPhrases(quizConverter.convertTranslation(nwQuizTranslation, quizId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(aLong -> getViewState().showProgress(false),
                        error -> {
                            getViewState().showProgress(false);
                            getViewState().showError(error.toString());
                        });
    }

    public Disposable addTranslation(String langCode, String translationText, String translationDescription) {
        return apiClient.addNwQuizTranslation(quizId, langCode, translationText, translationDescription)
                .map(nwQuiz -> quizDao.insertQuizWithQuizTranslations(quizConverter.convert(nwQuiz)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(aLong -> getViewState().showProgress(false),
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgress(false);
                        }
                );
    }

    public Disposable updateTranslationDescription(Long quizTranslationId, String description) {
        return apiClient.updateNwQuizTranslationDescription(quizTranslationId, description)
                .map(nwQuizTranslation -> quizDao.insertQuizTranslation(quizConverter.convertTranslation(nwQuizTranslation, quizId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(aLong -> getViewState().showProgress(false),
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgress(false);
                        }
                );
    }

    public Disposable deleteNwQuizById() {
        return apiClient.deleteNwQuizById(quizId)
                .map(aBoolean -> quizDao.deleteQuizById(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(integer -> {
                            getViewState().showProgress(false);
                            goToAllQuizFragment();
                        },
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgress(false);
                        }
                );
    }

    public Disposable deleteNwQuizTranslationById(Long nwQuizTranslationId) {
        return apiClient.deleteNwQuizTranslationById(nwQuizTranslationId)
                .map(aBoolean -> quizDao.deleteQuizTranslationById(nwQuizTranslationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(integer -> getViewState().showProgress(false),
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgress(false);
                        }
                );
    }


    public Disposable deleteNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId) {
        return apiClient.deleteNwQuizTranslationPhraseById(nwQuizTranslationPhraseId)
                .map(aBoolean -> quizDao.deleteQuizTranslationPhraseById(nwQuizTranslationPhraseId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(integer -> getViewState().showProgress(false),
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgress(false);
                        }
                );
    }

    public Disposable approveNwQuizById(Long nwQuizId, Boolean approve) {
        return apiClient.approveNwQuizById(nwQuizId, approve)
                .map(nwQuiz -> quizDao.insert(quizConverter.convert(nwQuiz)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .subscribe(aLong ->
                                getViewState().showProgress(false),
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgress(false);
                        });
    }

    private void goToAllQuizFragment() {
        router.backTo(Constants.ALL_QUIZ_SCREEN);
    }

//    public Disposable loadQuizFromDb() {
//        return quizDao.getQuizByIdOrErrorWithUpdates(quizId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(subscription -> getViewState().showProgress(true))
//                .subscribe(quiz -> {
//                    getViewState().showEditQuiz(quiz);
//                    getViewState().showProgress(false);
//                }, error -> {
//                    getViewState().showProgress(false);
//                    getViewState().showError(error.toString());
//                });
//    }
}