package com.scp.adminforquiz.mvp.viewinfo;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.db.Repository;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.api.NwUserAuthorities;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.scp.adminforquiz.util.SystemUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
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
public class AllQuizPresenter extends MvpPresenter<AllQuizView> {
    @Inject
    ApiClient apiClient;
    @Inject
    QuizConverter quizConverter;
    @Inject
    Repository repository;
    @Inject
    Router router;
    @Inject
    MyPreferenceManager preferences;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean dataUpdatedFromApi;
    private boolean userIsSet;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Flowable.combineLatest(
                repository.getAllQuizzes(),
                repository.getAllTranslations(),
                repository.getAllPhrases(),
                (Function3<List<Quiz>, List<QuizTranslation>, List<QuizTranslationPhrase>, Triple>) Triple::new
                )
                        .map(triple -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(listNotification -> {
                            if (!userIsSet) {
                                setUser();
                                userIsSet = true;
                            }
                        })
                        .subscribe(quizzes -> {
                                    if (!dataUpdatedFromApi) {
                                        loadQuizzesFromApi(1);
                                        dataUpdatedFromApi = true;
                                    }
                                    getViewState().showQuizList(quizzes);
                                },
                                error -> {
                                    Timber.e(error);
                                    getViewState().showError(error.toString());
                                })
        );
    }

    public void loadQuizzesFromApi(int page) {
//        getViewState().enableScrollListner(false);
//        if (page > 1) {
//            getViewState().showBottomProgress(true);
//        }
        compositeDisposable.add(apiClient.getAllWithUser()
                .map(nwQuizList -> quizConverter.convert(nwQuizList))
                .map(quizList -> repository.insertQuizzes(quizList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnEvent((longs, throwable) -> {
                    getViewState().showProgressBar(false);
                    getViewState().showSwipeRefresherBar(false);
//                    if (page > 1) {
//                        getViewState().showBottomProgress(false);
//                    }
                })
                .subscribe(longs -> {
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
            repository.deleteAllTables();
            preferences.setAccessToken(null);
            preferences.setRefreshToken(null);
            preferences.setUserSortFieldName(null);
            preferences.setUserFilterAscending(true);
            preferences.setUserId((long) 0);
            preferences.setIsAdmin(false);
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
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgressBar(true))
                .doOnTerminate(() -> {
                    getViewState().showProgressBar(false);
                    getViewState().showBottomSheet(false);
                })
                .subscribe(quizzes -> getViewState().showQuizList(quizzes),
                        error -> getViewState().showError(error.toString())
                ));
    }

    private void setUser() {
        compositeDisposable.add(
                apiClient.whoAreMe()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(user -> {
                            preferences.setUserId(user.id);
                            for (NwUserAuthorities nwUserAuthorities : user.authorities) {
                                if (nwUserAuthorities.authority.equals(Constants.ADMIN)) {
                                    preferences.setIsAdmin(true);
                                    break;
                                }
                                preferences.setIsAdmin(false);
                            }
                        })
                        .subscribe(user -> {
                                },
                                error -> getViewState().showError(error.toString()))
        );
    }

    public void filterByDateCreated() {
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.CREATED);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.UPDATED);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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
        preferences.setUserFilterAscending(true);
        preferences.setUserSortFieldName(Constants.APPROVE);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.ID);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.CREATED);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.UPDATED);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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
        preferences.setUserFilterAscending(false);
        preferences.setUserSortFieldName(Constants.APPROVE);
        compositeDisposable.add(Flowable.fromCallable(() -> repository.getQuizzesSorted(preferences.getUserFilterAscending(), preferences.getUserSortFieldName()))
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