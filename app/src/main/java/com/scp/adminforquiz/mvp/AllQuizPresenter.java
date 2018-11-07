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
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("FILTER ASCENDING , FILTER SORTFIELD : %s,%s", preferences.getUserFilterAscending(), preferences.getUserSortFieldName());
                        },
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
            preferences.setUserSortFieldName(null);
            preferences.setUserFilterAscending(true);
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
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.ID);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnTerminate(() -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateCreated() {
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.CREATED);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateUpdated() {
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.UPDATED);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByApproved() {
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.APPROVE);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByIdDesc() {
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.ID);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateCreatedDesc() {
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.CREATED);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByDateUpdatedDesc() {
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.UPDATED);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }

    public void filterByApprovedDesc() {
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.APPROVE);
        compositeDisposable.add(Flowable.fromCallable(() -> quizDao.getAllQuizzesWithTranslationsAndPhrases(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEach(listNotification -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> {
                            getViewState().showQuizList(quizzes);
                            Timber.d("SORT FIELD,ASCENDING:%s,%s", preferences.getUserSortFieldName(), preferences.getUserFilterAscending());
                        },
                        error -> getViewState().showError(error.toString())
                ));
    }
}