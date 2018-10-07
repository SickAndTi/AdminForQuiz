package com.example.user.adminforquiz.mvp;

import android.annotation.SuppressLint;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.api.NwQuizTranslation;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
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

    private Quiz quiz;

    public Quiz getQuiz() {
        return quiz;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Timber.d("onFirstViewAttach EditPresenter");
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        getQuizFromDb();
    }

    public void getQuizFromDb() {
        getQuizFRomDbSingle().subscribe(quiz -> {
            this.quiz = quiz;
            getViewState().showEditQuiz(quiz);
        });
    }

    private Single<Quiz> getQuizFRomDbSingle() {
        return Single.fromCallable(() -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void addTranslationPhrase(String translationPhrase) {
        apiClient.addNwQuizTranslationPhrase(quizId, translationPhrase)
                .flatMap(nwQuizTranslation -> apiClient.getNwQuizById(quizId))
                .map(nwQuiz -> quizDao.insert(quizConverter.convert(nwQuiz)))
                .flatMap(integer -> getQuizFRomDbSingle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doOnSuccess(quiz1 -> getViewState().showProgress(false))
                .subscribe(quiz1 -> {
                    this.quiz = quiz1;
                    getViewState().showEditQuiz(this.quiz);
                });
    }

    public void addTranslation(String langCode, String translationText, String translationDescription) {
        apiClient.addNwQuizTranslation(quizId, langCode, translationText, translationDescription)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updateTranslationDescription(Long quizTranslationId, String description) {
        apiClient.updateNwQuizTranslationDescription(quizTranslationId, description).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void deleteNwQuizById() {
        apiClient.deleteNwQuizById(quizId)
                .map(aBoolean -> quizDao.deleteQuizById(quizId))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((integer) -> goToAllQuizFragment());
    }

    private void goToAllQuizFragment() {
        router.navigateTo(Constants.ALL_QUIZ_SCREEN);
    }
}
