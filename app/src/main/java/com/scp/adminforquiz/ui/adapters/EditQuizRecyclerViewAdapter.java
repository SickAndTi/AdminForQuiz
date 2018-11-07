package com.scp.adminforquiz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.IconCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.scp.adminforquiz.R;
import com.scp.adminforquiz.di.GlideApp;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.ui.OneQuizRecyclerViewItem;
import com.scp.adminforquiz.util.DimensionUtils;
import com.haipq.android.flagkit.FlagImageView;

import java.util.ArrayList;
import java.util.List;

public class EditQuizRecyclerViewAdapter extends RecyclerView.Adapter {


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (oneQuizRecyclerViewItemList.get(position).type) {
            case QUIZ:
                EditOneQuizViewHolder viewHolder = (EditOneQuizViewHolder) holder;
                Quiz quiz = (Quiz) oneQuizRecyclerViewItemList.get(position).data;
                viewHolder.approved.setChecked(quiz.approved);
                viewHolder.tvScpNumber.setText(quiz.scpNumber);
                for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                    viewHolder.etEnterTitle.setText(quizTranslation.translation);
                    viewHolder.etEnterLangCode.setText(quizTranslation.langCode);
                    viewHolder.etEnterDescription.setText(quizTranslation.description);
                }
                GlideApp
                        .with(viewHolder.itemView.getContext())
                        .load(quiz.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(viewHolder.imageView);
                break;
            case QUIZ_TRANSLATION:
                EditOneQuizTranslationViewHolder editOneQuizTranslationViewHolder = (EditOneQuizTranslationViewHolder) holder;
                QuizTranslation quizTranslation = (QuizTranslation) oneQuizRecyclerViewItemList.get(position).data;
                editOneQuizTranslationViewHolder.tvQuizTitle.setText(quizTranslation.translation);
                editOneQuizTranslationViewHolder.tvQuizDescription.setText(quizTranslation.description);
                editOneQuizTranslationViewHolder.imvDeleteTranslation.setOnClickListener(v -> editInterface.onTranslationDeleteClicked(quizTranslation));
                editOneQuizTranslationViewHolder.imvUpdateDescription.setOnClickListener(v -> editInterface.onTranslationEditClicked(quizTranslation));
                editOneQuizTranslationViewHolder.etPhrasesLayout.removeAllViews();
                for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                    TextView textView = new TextView(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    textView.setText(quizTranslationPhrase.translation);
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(textView);
                    ImageView deleteImage = new ImageView(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    deleteImage.setImageResource(R.drawable.ic_delete_black_24dp);
                    deleteImage.setOnClickListener(v -> editInterface.onTranslationPhraseDeleteClicked(quizTranslationPhrase));
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(deleteImage);
                }
                editOneQuizTranslationViewHolder.tvQuizTitle.setOnClickListener(v -> editInterface.onTranslationAddPhraseClicked(quizTranslation));
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

        TextView tvScpNumber;
        Switch approved;
        ImageView imageView;
        EditText etEnterLangCode, etEnterTitle, etEnterDescription, etEnterPhrases;

        EditOneQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            approved = itemView.findViewById(R.id.approved);
            imageView = itemView.findViewById(R.id.imageView);
            etEnterLangCode = itemView.findViewById(R.id.etEnterLangCode);
            etEnterTitle = itemView.findViewById(R.id.etEnterTitle);
            etEnterDescription = itemView.findViewById(R.id.etEnterDescription);
            etEnterPhrases = itemView.findViewById(R.id.etEnterPhrases);

        }
    }

    static class EditOneQuizTranslationViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizTitle;
        TextView tvQuizDescription;
        LinearLayout etPhrasesLayout;
        ImageView imvDeleteTranslation, imvUpdateDescription;

        EditOneQuizTranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            etPhrasesLayout = itemView.findViewById(R.id.etPhrasesLayout);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvQuizDescription = itemView.findViewById(R.id.tvQuizDescription);
            imvDeleteTranslation = itemView.findViewById(R.id.imvDeleteTranslation);
            imvUpdateDescription = itemView.findViewById(R.id.imvUpdateDescription);
        }
    }
}
