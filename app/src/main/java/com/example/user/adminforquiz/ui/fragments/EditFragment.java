package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.mvp.EditPresenter;
import com.example.user.adminforquiz.mvp.EditView;
import com.example.user.adminforquiz.ui.adapters.EditQuizRecyclerViewAdapter;

import javax.inject.Inject;

import timber.log.Timber;

public class EditFragment extends MvpAppCompatFragment implements EditView {
    public final static String EXTRA_QUIZID = "EXTRA_QUIZID";
    @InjectPresenter
    EditPresenter editPresenter;
    @Inject
    QuizDao quizDao;
    RecyclerView recyclerViewEditQuiz;
    EditQuizRecyclerViewAdapter editQuizRecyclerViewAdapter;

    public static EditFragment newInstance(Long quizId) {
        EditFragment fragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZID, quizId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTranslation:
                addTranslation();
            case R.id.addTranslationPhrase:
                addTranslationPhrase();
            case R.id.saveChanges:
                saveChanges();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewEditQuiz = view.findViewById(R.id.recyclerViewEditQuiz);
        recyclerViewEditQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        editQuizRecyclerViewAdapter = new EditQuizRecyclerViewAdapter();
        recyclerViewEditQuiz.setAdapter(editQuizRecyclerViewAdapter);
    }

    @ProvidePresenter
    EditPresenter provideEditPresenter() {
        Timber.d("Provide Presenter");
        EditPresenter editPresenter = new EditPresenter();
        editPresenter.setQuizId(getArguments().getLong(EXTRA_QUIZID));
        return editPresenter;
    }

    @ProvidePresenterTag(presenterClass = EditPresenter.class)
    String provideEditPresenterTag() {
        Timber.d("Provide Presenter Tag");
        return String.valueOf(getArguments().getLong(EXTRA_QUIZID));
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEditQuiz(Quiz quiz) {
        Timber.d("show editQuiz %s", quiz);
        editQuizRecyclerViewAdapter.setEditQuiz(quiz);
    }

    @Override
    public void saveChanges() {
        //TODO saveChanges logic
    }


    @Override
    public void addTranslation() {
        //TODO logic addTranslation
    }

    @Override
    public void addTranslationPhrase() {
        //TODO logic addTranslationPhrase
    }
}
