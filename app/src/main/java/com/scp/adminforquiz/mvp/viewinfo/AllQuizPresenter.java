package com.scp.adminforquiz.mvp.viewinfo;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.db.QuizRepository;
import com.scp.adminforquiz.model.AllQuizData;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.model.api.NwUserAuthorities;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import java.util.ArrayList;
import java.util.List;

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
    QuizRepository quizRepository;
    @Inject
    Router router;
    @Inject
    MyPreferenceManager preferences;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean dataUpdatedFromApi;
    private boolean userIsSet;
    private List<Quiz> quizListFromDb = new ArrayList<>();


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(
                Flowable
                        .combineLatest(
                                quizRepository.getAllQuizzes(),
                                quizRepository.getAllTranslations(),
                                quizRepository.getAllPhrases(),
                                quizRepository.getUserFilterAscendingType(),
                                quizRepository.getUserSortFieldName(),
                                AllQuizData::new
                        )
                        .observeOn(Schedulers.io())
                        .map(allQuizData -> quizRepository.getQuizzesSorted(allQuizData.getUserFilterAscending(), allQuizData.getUserSortFieldName()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEach(listNotification -> {
                            if (!userIsSet) {
                                setUser();
                                userIsSet = true;
                            }
                        })
                        .subscribe(quizzes -> {
                                    quizListFromDb.clear();
                                    quizListFromDb.addAll(quizzes);
                                    if (!dataUpdatedFromApi) {
                                        loadQuizzesFromApi(Constants.OFFSET_ZERO);
                                        dataUpdatedFromApi = true;
                                    }
                                    getViewState().showQuizList(quizzes);
                                },
                                error -> {
                                    Timber.e(error);
                                    getViewState().showError(error.toString());
                                })
        );

        compositeDisposable.add(
                quizRepository.getUserFilterAscendingType()
                        .subscribe(filterAscendingType -> getViewState().setUserFilterAscendingType(filterAscendingType)
                        )
        );

        compositeDisposable.add(
                quizRepository.getUserSortFieldName()
                        .subscribe(sortFieldName -> getViewState().setUserSortFieldName(sortFieldName))
        );
    }

    public void onQueryTextChanged(String queryText) {
        getViewState().filterQueryText(queryText);
    }

    public void loadQuizzesFromApi(int offset) {
        compositeDisposable.add(
                apiClient.getAllWithUser(offset, Constants.PAGE_LIMIT)
                        .map(nwQuizList -> quizConverter.convert(nwQuizList))
                        .map(quizList -> {
                            if (offset == Constants.OFFSET_ZERO) {
                                quizRepository.deleteAllQuizTables();
                            }
                            return quizRepository.insertQuizzes(quizList);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                            if (quizListFromDb.isEmpty()) {
                                getViewState().showProgressBar(true);
                            } else {
                                if (offset > 0) {
                                    getViewState().showBottomProgress(true);
                                } else {
                                    getViewState().showSwipeProgressBar(true);
                                }
                            }
                        })
                        .doOnEvent((longs, throwable) -> {
                            getViewState().enableScrollListener(true);
                            getViewState().showProgressBar(false);
                            getViewState().showSwipeProgressBar(false);
                        })
                        .subscribe(longs -> {
                                },
                                error -> {
                                    Timber.e(error);
                                    getViewState().showError(error.toString());
                                }
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
            quizRepository.deleteAllQuizTables();
            preferences.setAccessToken(null);
            preferences.setRefreshToken(null);
            quizRepository.setUserSortFieldName(Constants.ID);
            quizRepository.setUserFilterAscendingType(true);
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

    public void filterById() {
        quizRepository.setUserFilterAscendingType(true);
        quizRepository.setUserSortFieldName(Constants.ID);
    }

    public void filterByDateCreated() {
        quizRepository.setUserFilterAscendingType(true);
        quizRepository.setUserSortFieldName(Constants.CREATED);
    }

    public void filterByDateUpdated() {
        quizRepository.setUserFilterAscendingType(true);
        quizRepository.setUserSortFieldName(Constants.UPDATED);
    }

    public void filterByApproved() {
        quizRepository.setUserFilterAscendingType(true);
        quizRepository.setUserSortFieldName(Constants.APPROVE);
    }

    public void filterByIdDesc() {
        quizRepository.setUserFilterAscendingType(false);
        quizRepository.setUserSortFieldName(Constants.ID);
    }

    public void filterByDateCreatedDesc() {
        quizRepository.setUserFilterAscendingType(false);
        quizRepository.setUserSortFieldName(Constants.CREATED);
    }

    public void filterByDateUpdatedDesc() {
        quizRepository.setUserFilterAscendingType(false);
        quizRepository.setUserSortFieldName(Constants.UPDATED);
    }

    public void filterByApprovedDesc() {
        quizRepository.setUserFilterAscendingType(false);
        quizRepository.setUserSortFieldName(Constants.APPROVE);
    }
}