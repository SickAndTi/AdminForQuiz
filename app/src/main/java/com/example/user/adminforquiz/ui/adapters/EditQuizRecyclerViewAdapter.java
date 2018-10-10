package com.example.user.adminforquiz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.di.GlideApp;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.QuizTranslationPhrase;
import com.example.user.adminforquiz.model.ui.OneQuizRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class EditQuizRecyclerViewAdapter extends RecyclerView.Adapter {
    EditInterface editInterface;

    public interface EditInterface {
        void onTranslationEditClicked(QuizTranslation quizTranslation);

        void onTranslationDeleteClicked(QuizTranslation quizTranslation);

        void onTranslationPhraseDeleteClicked(QuizTranslationPhrase quizTranslationPhrase);

        void onTranslationAddPhraseClicked(QuizTranslation quizTranslation);
    }

    public EditQuizRecyclerViewAdapter(EditInterface editInterface) {
        this.editInterface = editInterface;
    }

    private List<OneQuizRecyclerViewItem> oneQuizRecyclerViewItemList = new ArrayList<>();

    public void setEditQuiz(Quiz quiz) {
        oneQuizRecyclerViewItemList.clear();
        oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quiz, OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ));
        for (int i = 0; i < quiz.quizTranslations.size(); i++) {
            oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quiz.quizTranslations.get(i), OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ_TRANSLATION));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (OneQuizRecyclerViewItem.RecyclerAdapterItemType.values()[viewType]) {
            case QUIZ:
                return new EditOneQuizViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_edit_fragment, viewGroup, false));
            case QUIZ_TRANSLATION:
                return new EditOneQuizTranslationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_edit_translation, viewGroup, false));
            default:
                throw new IllegalArgumentException("Wrong viewType");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (oneQuizRecyclerViewItemList.get(position).type) {
            case QUIZ:
                EditOneQuizViewHolder editOneQuizViewHolder = (EditOneQuizViewHolder) viewHolder;
                Quiz quiz = (Quiz) oneQuizRecyclerViewItemList.get(position).data;
                editOneQuizViewHolder.approved.setChecked(quiz.approved);
                editOneQuizViewHolder.etScpNumber.setText(quiz.scpNumber);
                GlideApp
                        .with(viewHolder.itemView.getContext())
                        .load(quiz.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(editOneQuizViewHolder.imageView);
                break;
            case QUIZ_TRANSLATION:
                EditOneQuizTranslationViewHolder editOneQuizTranslationViewHolder = (EditOneQuizTranslationViewHolder) viewHolder;
                QuizTranslation quizTranslation = (QuizTranslation) oneQuizRecyclerViewItemList.get(position).data;
                editOneQuizTranslationViewHolder.etQuizTitle.setText(quizTranslation.translation);
                editOneQuizTranslationViewHolder.etQuizDescription.setText(quizTranslation.description);
                editOneQuizTranslationViewHolder.btnDeleteTranslation.setOnClickListener(v -> editInterface.onTranslationDeleteClicked(quizTranslation));
                editOneQuizTranslationViewHolder.etPhrasesLayout.removeAllViews();
//                for (int i = 0; i < quizTranslation.quizTranslationPhrases.size(); i++) {
                for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                    EditText editText = new EditText(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    editText.setText(quizTranslationPhrase.translation);
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(editText);
                    Button btnDeleteTranslationPhrase = new Button(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    btnDeleteTranslationPhrase.setText(R.string.deleteTranslationPhrase);
                    btnDeleteTranslationPhrase.setOnClickListener(v -> editInterface.onTranslationPhraseDeleteClicked(quizTranslationPhrase));
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(btnDeleteTranslationPhrase);
                }
                editOneQuizTranslationViewHolder.etQuizDescription.setOnClickListener(v -> editInterface.onTranslationEditClicked(quizTranslation));
                editOneQuizTranslationViewHolder.etQuizTitle.setOnClickListener(v -> editInterface.onTranslationAddPhraseClicked(quizTranslation));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return oneQuizRecyclerViewItemList.size();

    }

    @Override
    public int getItemViewType(int position) {
        return oneQuizRecyclerViewItemList.get(position).type.ordinal();
    }

    static class EditOneQuizViewHolder extends RecyclerView.ViewHolder {

        EditText etScpNumber;
        Switch approved;
        ImageView imageView;

        EditOneQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            etScpNumber = itemView.findViewById(R.id.etScpNumber);
            approved = itemView.findViewById(R.id.approved);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    static class EditOneQuizTranslationViewHolder extends RecyclerView.ViewHolder {
        EditText etQuizTitle;
        EditText etQuizDescription;
        LinearLayout etPhrasesLayout;
        Button btnDeleteTranslation;

        EditOneQuizTranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            etPhrasesLayout = itemView.findViewById(R.id.etPhrasesLayout);
            etQuizTitle = itemView.findViewById(R.id.etQuizTitle);
            etQuizDescription = itemView.findViewById(R.id.etQuizDescription);
            btnDeleteTranslation = itemView.findViewById(R.id.btnDeleteTranslation);
        }
    }
}
