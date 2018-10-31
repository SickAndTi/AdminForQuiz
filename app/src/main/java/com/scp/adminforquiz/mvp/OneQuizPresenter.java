package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.db.dao.QuizDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import kotlin.Triple;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class OneQuizPresenter extends MvpPresenter<OneQuizView> {

    @Inject
    QuizDao quizDao;
    @Inject
    Router router;
    private Long quizId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Flowable.combineLatest(
                quizDao.getQuizByIdOrErrorWithUpdates(quizId),
                quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId),
                quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId),
                (Function3<Quiz, List<QuizTranslation>, List<QuizTranslationPhrase>, Triple>) Triple::new
        )
                .map(o -> quizDao.getQuizWithTranslationsAndPhrases(quizId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quiz -> getViewState().showQuiz(quiz)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void goToEditQuiz() {
        router.navigateTo(Constants.EDIT_SCREEN, quizId);
    }
}