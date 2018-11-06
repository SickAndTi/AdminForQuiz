package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.dao.QuizDao;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
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
        loadQuizzesFromPage(1);
    }

    public void loadQuizzesFromPage(int page) {
//        getViewState().enableScrollListner(false);
        if (page > 1) {
            getViewState().showBottomProgress(true);
        }
        compositeDisposable.add(apiClient.getAllWithUser()
                .map(nwQuizList -> quizConverter.convert(nwQuizList))
                .map(quizList -> quizDao.insertQuizesWithQuizTranslations(quizList))
                .map(longs -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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

    public void filterById() {
        compositeDisposable.add(Flowable.fromCallable(() -> {
            preferences.setUserFilterAscending(true);
            preferences.setUserSortFieldName("id");
            return preferences.getUserSortFieldName();
        })
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD:%s", preferences.getUserSortFieldName());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateCreated() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(true);
            preferences.setUserSortFieldName("created");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateUpdated() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(true);
            preferences.setUserSortFieldName("updated");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByApproved() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(true);
            preferences.setUserSortFieldName("approve");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByIdDesc() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(false);
            preferences.setUserSortFieldName("id");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateCreatedDesc() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(false);
            preferences.setUserSortFieldName("created");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateUpdatedDesc() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(false);
            preferences.setUserSortFieldName("updated");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByApprovedDesc() {
        compositeDisposable.add(Completable.fromAction(() -> {
            preferences.setUserFilterAscending(false);
            preferences.setUserSortFieldName("approve");
        })
                .toFlowable()
                .map(t -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }
}