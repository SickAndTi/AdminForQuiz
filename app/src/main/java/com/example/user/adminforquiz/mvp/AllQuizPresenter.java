package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.Completable;
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
        loadDataFromApiAndGetFromDb();
    }

    public void loadDataFromApiAndGetFromDb() {
        compositeDisposable.add(apiClient.getNwQuizList()
                .doOnEvent((nwQuizs, error) -> quizDao.deleteAllTables())
                .map(nwQuizs -> quizConverter.convert(nwQuizs))
                .map(quizzes -> quizDao.insertQuizesWithQuizTranslations(quizzes))
                .toFlowable()
                .flatMap(longs -> quizDao.getAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach((notification) -> {
                    getViewState().showProgressBar(false);
                    getViewState().showSwipeRefresherBar(false);
                })
                .subscribe(listFlowable -> getViewState().showQuizList(listFlowable),
                        error -> getViewState().showError(error.toString()))
        );
    }

    public void goToQuizFragment(Quiz quiz) {
        router.navigateTo(Constants.ONE_QUIZ_SCREEN, quiz.id);
    }

    public void createNwQuiz(String scpNumber, String imageUrl) {
        NwQuiz nwQuiz = new NwQuiz();
        nwQuiz.scpNumber = scpNumber;
        nwQuiz.imageUrl = imageUrl;
        compositeDisposable.add(apiClient.createNwQuiz(nwQuiz)
                .toFlowable()
                .map(nwQuiz1 -> quizConverter.convert(nwQuiz1))
                .map(quiz -> quizDao.insert(quiz))
                .flatMap(aLong -> quizDao.getAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> getViewState().showProgressBar(true))
                .doOnEach((notification) -> getViewState().showProgressBar(false))
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
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