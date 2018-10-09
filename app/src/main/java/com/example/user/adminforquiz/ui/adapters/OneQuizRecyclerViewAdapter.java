package com.example.user.adminforquiz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.di.GlideApp;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.ui.OneQuizRecyclerViewItem;
import com.example.user.adminforquiz.util.DateTypeConverter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class OneQuizRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<OneQuizRecyclerViewItem> oneQuizRecyclerViewItemList = new ArrayList<>();

    public void setQuiz(Quiz quiz) {
        oneQuizRecyclerViewItemList.clear();
        oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quiz, OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ));
        for (int i = 0; i < quiz.quizTranslations.size(); i++) {
            oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quiz.quizTranslations.get(i), OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ_TRANSLATION));
        }
        notifyDataSetChanged();
    }


    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (OneQuizRecyclerViewItem.RecyclerAdapterItemType.values()[viewType]) {
            case QUIZ:
                return new OneQuizViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_single, parent, false));
            case QUIZ_TRANSLATION:
                return new OneQuizTranslationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_translations_and_phrases, parent, false));
            default:
                throw new IllegalArgumentException("unexpected viewType");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return oneQuizRecyclerViewItemList.get(position).type.ordinal();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (oneQuizRecyclerViewItemList.get(position).type) {
            case QUIZ:
                OneQuizViewHolder viewHolder = (OneQuizViewHolder) holder;
                Quiz quiz = (Quiz) oneQuizRecyclerViewItemList.get(position).data;
                viewHolder.tvScpNumber.setText(quiz.scpNumber);
                viewHolder.approved.setChecked(quiz.approved);
                viewHolder.dateCreated.setText(DateTypeConverter.formatDate(quiz.created));
                viewHolder.dateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
                viewHolder.ruTranslation.setImageResource(R.drawable.rflag);
                viewHolder.enTranslation.setImageResource(R.drawable.gbrflag);
                viewHolder.btnAdd.setImageResource(R.drawable.btnadd);
                viewHolder.btnDelete.setImageResource(R.drawable.btndelete);
                viewHolder.authorId.setText(String.valueOf(quiz.authorId));
                viewHolder.approverId.setText(String.valueOf(quiz.approverId));
                GlideApp
                        .with(holder.itemView.getContext())
                        .load(quiz.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(viewHolder.imageView);
                break;
            case QUIZ_TRANSLATION:
                OneQuizTranslationViewHolder oneQuizTranslationViewHolder = (OneQuizTranslationViewHolder) holder;
                QuizTranslation quizTranslation = (QuizTranslation) oneQuizRecyclerViewItemList.get(position).data;
                oneQuizTranslationViewHolder.quizTitle.setText(quizTranslation.translation);
                oneQuizTranslationViewHolder.quizDescription.setText(quizTranslation.description);
                oneQuizTranslationViewHolder.phrasesLayout.removeAllViews();
                for (int i = 0; i < quizTranslation.quizTranslationPhrases.size(); i++) {
                    TextView textView = new TextView(oneQuizTranslationViewHolder.phrasesLayout.getContext());
                    textView.setText(quizTranslation.quizTranslationPhrases.get(i).translation);
                    oneQuizTranslationViewHolder.phrasesLayout.addView(textView);
                }
                Timber.d(quizTranslation.toString());
                break;
        }
    }

    @Override
    public int getItemCount() {
        Timber.d("%s getItemCount", oneQuizRecyclerViewItemList.size());
        return oneQuizRecyclerViewItemList.size();
    }

    static class OneQuizViewHolder extends RecyclerView.ViewHolder {

        TextView tvScpNumber, approverId, authorId;
        Switch approved;
        ImageView btnDelete, btnAdd, ruTranslation, enTranslation;
        ImageView imageView;
        TextView dateCreated, dateUpdated;

        OneQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            approved = itemView.findViewById(R.id.approved);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ruTranslation = itemView.findViewById(R.id.ruTranslation);
            enTranslation = itemView.findViewById(R.id.enTranslation);
            imageView = itemView.findViewById(R.id.imageView);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            authorId = itemView.findViewById(R.id.authorId);
            approverId = itemView.findViewById(R.id.approverId);
        }
    }

    static class OneQuizTranslationViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle;
        TextView quizDescription;
        LinearLayout phrasesLayout;

        OneQuizTranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            phrasesLayout = itemView.findViewById(R.id.phrasesLayout);
            quizTitle = itemView.findViewById(R.id.quizTitle);
            quizDescription = itemView.findViewById(R.id.quizDescription);
        }
    }
}
