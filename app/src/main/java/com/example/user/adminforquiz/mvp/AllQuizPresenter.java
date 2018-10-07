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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        getViewState().showProgressBar(true);
        loadDataFromApi();
        quizDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quizzes -> {
                    getViewState().showQuizList(quizzes);
                    getViewState().showProgressBar(false);
                });
    }

    public void loadDataFromApi() {
        apiClient.getAccessToken()
                .flatMap(tokenResponse -> apiClient.getNwQuizList())
                .map(nwQuizList -> quizConverter.convert(nwQuizList))
                .map(quizList -> quizDao.insertQuizesWithQuizTranslations(quizList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> getViewState().showSwipeRefresherBar(false),
                        error -> {
                            Timber.d(error);
                            getViewState().showSwipeRefresherBar(false);
                            getViewState().showError(error.toString());
                            getViewState().showProgressBar(false);
                        }
                );
    }

    public void goToQuiz(Quiz quiz) {
        router.navigateTo(Constants.ONE_QUIZ_SCREEN, quiz.id);
    }

    public Disposable createNwQuiz(String scpNumber, String imageUrl) {
        NwQuiz nwQuiz = new NwQuiz();
        nwQuiz.scpNumber = scpNumber;
        nwQuiz.imageUrl = imageUrl;
        return apiClient.createNwQuiz(nwQuiz)
                .map(nwQuiz1 -> quizConverter.convert(nwQuiz1))
                .map(quiz -> quizDao.insert(quiz))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> goToAllQuizFragment());
    }

    private void goToAllQuizFragment() {
        router.navigateTo(Constants.ALL_QUIZ_SCREEN);
    }
}
