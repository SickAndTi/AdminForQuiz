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
import timber.log.Timber;
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
        loadDataFromApi();
        compositeDisposable.add(quizDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> getViewState().showProgressBar(true))
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            getViewState().showProgressBar(false);
                        },
                        error -> {
                            getViewState().showProgressBar(false);
                            getViewState().showError(error.toString());
                        }));
    }

    public void loadDataFromApi() {
        compositeDisposable.add(apiClient.getNwQuizList()
                .flatMap(tokenResponse -> apiClient.getNwQuizList())
                .map(nwQuizList -> quizConverter.convert(nwQuizList))
                .map(quizList -> quizDao.insertQuizesWithQuizTranslations(quizList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .subscribe(ignore -> getViewState().showSwipeRefresherBar(false),
                        error -> {
                            Timber.d(error);
                            getViewState().showSwipeRefresherBar(false);
                            getViewState().showError(error.toString());
                            getViewState().showProgressBar(false);
                        }
                ));
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
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            getViewState().showProgressBar(false);
                        },
                        error -> {
                            getViewState().showProgressBar(false);
                            getViewState().showError(error.toString());
                        }));
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
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            router.newRootScreen(Constants.AUTH_SCREEN);
                            getViewState().showProgressBar(false);
                        },
                        error -> {
                            getViewState().showError(error.toString());
                            getViewState().showProgressBar(false);
                        }));
    }
}
