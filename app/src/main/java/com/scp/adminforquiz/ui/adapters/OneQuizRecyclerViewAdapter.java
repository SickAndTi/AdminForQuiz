package com.scp.adminforquiz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.scp.adminforquiz.util.DateTypeConverter;
import com.scp.adminforquiz.util.DimensionUtils;
import com.haipq.android.flagkit.FlagImageView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class OneQuizRecyclerViewAdapter extends RecyclerView.Adapter {

    private EditInterface editInterface;

    public interface EditInterface {
        void onTranslationEditClicked(QuizTranslation quizTranslation);

        void onTranslationDeleteClicked(QuizTranslation quizTranslation);

        void onTranslationPhraseDeleteClicked(QuizTranslationPhrase quizTranslationPhrase);

        void onTranslationAddPhraseClicked(QuizTranslation quizTranslation);

        void onApproveQuizClicked(Quiz quiz);
    }

    public OneQuizRecyclerViewAdapter(EditInterface editInterface) {
        this.editInterface = editInterface;
    }

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
                viewHolder.dateCreated.setText(DateTypeConverter.formatDate(quiz.created));
                viewHolder.dateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
                viewHolder.approveQuiz.setChecked(quiz.approved);
                viewHolder.approveQuiz.setOnClickListener(v -> editInterface.onApproveQuizClicked(quiz));
                viewHolder.flagLayout.removeAllViews();
                for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                    FlagImageView flagImage = new FlagImageView(viewHolder.flagLayout.getContext());
                    if (quizTranslation.langCode.contains("en")) {
                        flagImage.setCountryCode("gb");
                        flagImage.setMaxWidth(DimensionUtils.convertDpToPixels(70));
                        flagImage.setMaxHeight(DimensionUtils.convertDpToPixels(50));
                        flagImage.setPadding(DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8));
                        viewHolder.flagLayout.addView(flagImage);
                    } else if (quizTranslation.langCode.contains("ru")) {
                        flagImage.setCountryCode("ru");
                        flagImage.setMaxWidth(DimensionUtils.convertDpToPixels(70));
                        flagImage.setMaxHeight(DimensionUtils.convertDpToPixels(50));
                        flagImage.setPadding(DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8));
                        viewHolder.flagLayout.addView(flagImage);
                    }
                }
                GlideApp
                        .with(holder.itemView.getContext())
                        .load(quiz.imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(viewHolder.imageView);
                GlideApp
                        .with(holder.itemView.getContext())
                        .load(quiz.author.avatar)
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(viewHolder.userIcon);
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
                break;
        }
    }

    @Override
    public int getItemCount() {
        return oneQuizRecyclerViewItemList.size();
    }

    static class OneQuizViewHolder extends RecyclerView.ViewHolder {

        TextView tvScpNumber;
        ImageView imageView, userIcon;
        Switch approveQuiz;
        TextView dateCreated, dateUpdated;
        LinearLayout flagLayout;

        OneQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            imageView = itemView.findViewById(R.id.imageView);
            userIcon = itemView.findViewById(R.id.userIcon);
            approveQuiz = itemView.findViewById(R.id.approveQuiz);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            flagLayout = itemView.findViewById(R.id.flagLayout);
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
