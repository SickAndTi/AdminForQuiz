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
//                .doOnNext((Object[] o) -> Timber.d("CHANGES!!! %s", Arrays.toString(o)))
//                .doOnNext((Object[] o) -> Timber.d("CHANGES!!! AS LIST %s", Arrays.asList(o)))
//                .doOnNext((Object[] o) -> Timber.d("CHANGES!!! TRI O %s,%s,%s", o, o, o))
//                .doOnNext((Object[] o) -> System.out.println(String.format("CHANGES!!! STRING FORMAT  %s,%s,%s,%s", o)))
//                .doOnNext((Object[] o) -> Timber.d("CHANGES!!! JUST TIMBER %s,%s,%s", o))
                .doOnNext(triple -> Timber.d("TRIPLE REKT,%s", triple))
                .map(o -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz -> getViewState().showQuiz(quiz));
        quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(quizTranslations -> Timber.d("FAK %s,", quizTranslations));
        quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(quizTranslationPhraseList -> Timber.d("FAK PHRASES %s,", quizTranslationPhraseList));
    }

//    @Override
//    public void attachView(OneQuizView view) {
//        super.attachView(view);
//        Flowable.fromCallable(() -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(quiz -> getViewState().showQuiz(quiz));
//    }

    public void goToEditQuiz() {
        router.navigateTo(Constants.EDIT_SCREEN, quizId);
    }
}
