package com.example.user.adminforquiz.mvp;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import toothpick.Toothpick;

@InjectViewState
public class EditPresenter extends MvpPresenter<EditView> {
    @Inject
    QuizDao quizDao;
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
        super.onFirstViewAttach();
        Timber.d("onFirstViewAttach EditPresenter");
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        Flowable.fromCallable(() -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz -> getViewState().showEditQuiz(quiz));
    }
}
