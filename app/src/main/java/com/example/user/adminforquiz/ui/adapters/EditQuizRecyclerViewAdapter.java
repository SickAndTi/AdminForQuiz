package com.example.user.adminforquiz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.di.GlideApp;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.ui.OneQuizRecyclerViewItem;
import com.example.user.adminforquiz.util.DateTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class EditQuizRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<OneQuizRecyclerViewItem> oneQuizRecyclerViewItemList = new ArrayList<>();

    public void setEditQuiz(Quiz quiz) {
        oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quiz, OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ));
        for (int i = 0; i < oneQuizRecyclerViewItemList.size(); i++) {
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
                editOneQuizViewHolder.etApproverId.setText(String.valueOf(quiz.approverId));
                editOneQuizViewHolder.etAuthorId.setText(String.valueOf(quiz.authorId));
                editOneQuizViewHolder.etDateCreated.setText(DateTypeConverter.formatDate(quiz.created));
                editOneQuizViewHolder.etDateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
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
                editOneQuizTranslationViewHolder.etPhrasesLayout.removeAllViews();
                for (int i = 0; i < quizTranslation.quizTranslationPhrases.size(); i++) {
                    EditText editText = new EditText(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    editText.setText(quizTranslation.quizTranslationPhrases.get(i).translation);
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(editText);
                }
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
        EditText etAuthorId, etApproverId, etDateCreated, etDateUpdated;
        Switch approved;
        ImageView imageView;

        EditOneQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            etScpNumber = itemView.findViewById(R.id.etScpNumber);
            approved = itemView.findViewById(R.id.approved);
            imageView = itemView.findViewById(R.id.imageView);
            etDateCreated = itemView.findViewById(R.id.etDateCreated);
            etDateUpdated = itemView.findViewById(R.id.etDateUpdated);
            etAuthorId = itemView.findViewById(R.id.etAuthorId);
            etApproverId = itemView.findViewById(R.id.etApproverId);
        }
    }

    static class EditOneQuizTranslationViewHolder extends RecyclerView.ViewHolder {
        EditText etQuizTitle;
        EditText etQuizDescription;
        LinearLayout etPhrasesLayout;

        EditOneQuizTranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            etPhrasesLayout = itemView.findViewById(R.id.etPhrasesLayout);
            etQuizTitle = itemView.findViewById(R.id.etQuizTitle);
            etQuizDescription = itemView.findViewById(R.id.etQuizDescription);
        }
    }
}
