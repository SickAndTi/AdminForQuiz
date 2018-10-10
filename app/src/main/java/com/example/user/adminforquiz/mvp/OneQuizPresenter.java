package com.example.user.adminforquiz.mvp;

import android.annotation.SuppressLint;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Triple;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;
import toothpick.Toothpick;

@InjectViewState
public class OneQuizPresenter extends MvpPresenter<OneQuizView> {

    @Inject
    QuizDao quizDao;
    @Inject
    Router router;
    private Long quizId;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onFirstViewAttach() {
        Timber.d("onFirstViewAttach:%s", quizId);
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
//        Flowable.fromCallable(() -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(quiz -> getViewState().showQuiz(quiz));
//        List<Flowable<?>> flowableList = new ArrayList<>();
//        flowableList.add(quizDao.getQuizByIdOrErrorWithUpdates(quizId));
//        flowableList.add(quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId));
//        flowableList.add(quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId));
//        Flowable.combineLatest(flowableList, (Object[] objects) -> objects)
        Flowable.combineLatest(
                quizDao.getQuizByIdOrErrorWithUpdates(quizId),
                quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId),
                quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId),
                (quiz, quizTranslations, quizTranslationPhraseList) -> new Triple(quiz, quizTranslations, quizTranslationPhraseList)
        )
                .doOnNext(triple -> Timber.d("GETTING CHANGES BY TRIPPLE,%s", triple))
                .map(o -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz -> getViewState().showQuiz(quiz));
    }

    public void goToEditQuiz() {
        router.navigateTo(Constants.EDIT_SCREEN, quizId);
    }
}
