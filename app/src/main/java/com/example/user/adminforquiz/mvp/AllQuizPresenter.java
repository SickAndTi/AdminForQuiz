package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class AllQuizPresenter extends MvpPresenter<AllQuizView> {
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

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        loadQuizzesFromPage(1);
//        setQuizzesFromDb();
    }

    public void loadQuizzesFromPage(int page) {
//        getViewState().enableScrollListner(false);
        if (page > 1) {
            getViewState().showBottomProgress(true);
        }
        compositeDisposable.add(apiClient.getNwQuizList()
                .map(nwQuizList -> quizConverter.convert(nwQuizList))
                .map(quizList -> quizDao.insertQuizesWithQuizTranslations(quizList))
                .map(longs -> quizDao.getAllQuizzesWithTranslationsAndPhrases())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((longs, throwable) -> {
                    getViewState().showProgressBar(false);
                    getViewState().showSwipeRefresherBar(false);
                    if (page > 1) {
                        getViewState().showBottomProgress(false);
                    }
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void setQuizzesFromDb() {
        compositeDisposable.add(
                Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(subscription -> getViewState().showProgressBar(true))
                        .doOnEach(notification -> getViewState().showProgressBar(false))
                        .subscribe(quizzes -> {
                                    getViewState().showSwipeRefresherBar(false);
                                    getViewState().showQuizList(quizzes);
//                                    getViewState().enableScrollListner(true);
                                },
                                error -> {
                                    getViewState().showError(error.toString());
                                    getViewState().showSwipeRefresherBar(false);
                                })
        );
    }

    public void goToQuizFragment(Quiz quiz) {
        router.navigateTo(Constants.ONE_QUIZ_SCREEN, quiz.id);
    }

    public void goToCreateQuizFragment() {
        router.navigateTo(Constants.CREATE_QUIZ_SCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void logout() {
        compositeDisposable.add(Completable.fromAction(() -> {
            quizDao.deleteAllTables();
            preferences.setUserForAuth(null);
            preferences.setPasswordForAuth(null);
            preferences.setAccessToken(null);
            preferences.setRefreshToken(null);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnTerminate(() -> getViewState().showProgressBar(false))
                .subscribe(() -> router.newRootScreen(Constants.AUTH_SCREEN),
                        error -> getViewState().showError(error.toString())
                ));
    }
}