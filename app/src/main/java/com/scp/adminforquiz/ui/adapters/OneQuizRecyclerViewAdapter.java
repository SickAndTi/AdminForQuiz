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

import com.bumptech.glide.request.RequestOptions;
import com.haipq.android.flagkit.FlagImageView;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.di.GlideApp;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.ui.OneQuizRecyclerViewItem;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.scp.adminforquiz.util.DateTypeConverter;
import com.scp.adminforquiz.util.DimensionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;

public class OneQuizRecyclerViewAdapter extends RecyclerView.Adapter {
    @Inject
    MyPreferenceManager preferences;
    private EditInterface editInterface;


    @Inject
    OneQuizRecyclerViewAdapter() {
    }

    public interface EditInterface {

        void onTranslationDeleteClicked(QuizTranslation quizTranslation);

        void onTranslationPhraseDeleteClicked(QuizTranslationPhrase quizTranslationPhrase);

        void onTranslationAddPhraseClicked(QuizTranslation quizTranslation);

        void onApproveQuizClicked(Quiz quiz);

        void onApproveTranslationClicked(QuizTranslation quizTranslation);

        void onApprovePhraseClicked(QuizTranslationPhrase quizTranslationPhrase, Long quizTranslationId);
    }

    public OneQuizRecyclerViewAdapter(EditInterface editInterface) {
        this.editInterface = editInterface;
    }

    private List<OneQuizRecyclerViewItem> oneQuizRecyclerViewItemList = new ArrayList<>();

    public void setQuiz(Quiz quiz) {
        oneQuizRecyclerViewItemList.clear();
        oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quiz, OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ));
        for (QuizTranslation quizTranslation : quiz.quizTranslations) {
            oneQuizRecyclerViewItemList.add(new OneQuizRecyclerViewItem(quizTranslation, OneQuizRecyclerViewItem.RecyclerAdapterItemType.QUIZ_TRANSLATION));
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
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        switch (oneQuizRecyclerViewItemList.get(position).type) {
            case QUIZ:
                OneQuizViewHolder viewHolder = (OneQuizViewHolder) holder;
                Quiz quiz = (Quiz) oneQuizRecyclerViewItemList.get(position).data;
                viewHolder.tvScpNumber.setText(quiz.scpNumber);
                viewHolder.dateCreated.setText(DateTypeConverter.formatDate(quiz.created));
                viewHolder.dateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
                viewHolder.approveQuiz.setChecked(quiz.approved);
                viewHolder.approveQuiz.setEnabled(preferences.getIsAdmin());
                viewHolder.approveQuiz.setOnClickListener(v -> editInterface.onApproveQuizClicked(quiz));
                viewHolder.flagLayout.removeAllViews();
                for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                    String correctLangCode = quizTranslation.langCode;
                    FlagImageView flagImage = new FlagImageView(viewHolder.flagLayout.getContext());
                    if (quizTranslation.langCode.equals("en")) {
                        correctLangCode = "gb";
                    }
                    flagImage.setCountryCode(correctLangCode);
                    flagImage.setMaxWidth(DimensionUtils.convertDpToPixels(70));
                    flagImage.setMaxHeight(DimensionUtils.convertDpToPixels(50));
                    flagImage.setPadding(DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8), DimensionUtils.convertDpToPixels(8));
                    viewHolder.flagLayout.addView(flagImage);
                }
                GlideApp
                        .with(holder.itemView.getContext())
                        .load(quiz.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(viewHolder.imageView);
                if (quiz.author != null) {
                    GlideApp
                            .with(holder.itemView.getContext())
                            .load(quiz.author.avatar)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(viewHolder.userIcon);
                }
                break;
            case QUIZ_TRANSLATION:
                OneQuizTranslationViewHolder oneQuizTranslationViewHolder = (OneQuizTranslationViewHolder) holder;
                QuizTranslation quizTranslation = (QuizTranslation) oneQuizRecyclerViewItemList.get(position).data;
                oneQuizTranslationViewHolder.tvQuizTitle.setText(quizTranslation.translation);
                oneQuizTranslationViewHolder.tvQuizDescription.setText(quizTranslation.description);
                oneQuizTranslationViewHolder.approveTranslation.setChecked(quizTranslation.approved);
                oneQuizTranslationViewHolder.approveTranslation.setEnabled(preferences.getIsAdmin());
                oneQuizTranslationViewHolder.approveTranslation.setOnClickListener(v -> editInterface.onApproveTranslationClicked(quizTranslation));
                oneQuizTranslationViewHolder.imvDeleteTranslation.setOnClickListener(v -> editInterface.onTranslationDeleteClicked(quizTranslation));
                oneQuizTranslationViewHolder.imvAddPhrase.setOnClickListener(v -> editInterface.onTranslationAddPhraseClicked(quizTranslation));
                oneQuizTranslationViewHolder.imvDropDown.setOnClickListener(v -> {
                    if (oneQuizTranslationViewHolder.phrasesLayout.getVisibility() == View.GONE) {
                        oneQuizTranslationViewHolder.phrasesLayout.setVisibility(View.VISIBLE);
                        oneQuizTranslationViewHolder.imvDropDown.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                    } else if (oneQuizTranslationViewHolder.phrasesLayout.getVisibility() == View.VISIBLE) {
                        oneQuizTranslationViewHolder.phrasesLayout.setVisibility(View.GONE);
                        oneQuizTranslationViewHolder.imvDropDown.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    }
                });
                if (quizTranslation.langCode.contains("en")) {
                    oneQuizTranslationViewHolder.imvFlag.setCountryCode("gb");
                } else {
                    oneQuizTranslationViewHolder.imvFlag.setCountryCode(quizTranslation.langCode);
                }
                oneQuizTranslationViewHolder.tvLangCode.setText(quizTranslation.langCode);
                if (quizTranslation.author != null) {
                    oneQuizTranslationViewHolder.tvUserName.setText(quizTranslation.author.fullName);
                } else {
                    oneQuizTranslationViewHolder.tvUserName.setText(null);
                }
                if (quizTranslation.author != null) {
                    GlideApp
                            .with(holder.itemView.getContext())
                            .load(quizTranslation.author.avatar)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(oneQuizTranslationViewHolder.userIcon);
                }

                oneQuizTranslationViewHolder.phrasesLayout.removeAllViews();
                for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                    View phraseView = LayoutInflater.from(oneQuizTranslationViewHolder.itemView.getContext()).inflate(R.layout.phrases_layout, oneQuizTranslationViewHolder.phrasesLayout, false);
                    PhraseViewHolder phraseViewHolder = new PhraseViewHolder(phraseView);
                    phraseViewHolder.tvPhraseText.setText(quizTranslationPhrase.translation);
                    if (quizTranslationPhrase.author != null) {
                        phraseViewHolder.tvUserName.setText(quizTranslationPhrase.author.fullName);
                    }
                    phraseViewHolder.approvePhrase.setChecked(quizTranslationPhrase.approved);
                    phraseViewHolder.approvePhrase.setEnabled(preferences.getIsAdmin());
                    phraseViewHolder.approvePhrase.setOnClickListener(v -> editInterface.onApprovePhraseClicked(quizTranslationPhrase, quizTranslation.id));
                    phraseViewHolder.imvDeletePhrase.setOnClickListener(v -> editInterface.onTranslationPhraseDeleteClicked(quizTranslationPhrase));
                    if (quizTranslationPhrase.author != null) {
                        GlideApp
                                .with(holder.itemView.getContext())
                                .load(quizTranslationPhrase.author.avatar)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(phraseViewHolder.userIcon);
                    }
                    oneQuizTranslationViewHolder.phrasesLayout.addView(phraseView);
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
        TextView tvQuizTitle;
        TextView tvQuizDescription;
        LinearLayout phrasesLayout;
        ImageView imvDeleteTranslation;
        Switch approveTranslation;
        ImageView imvDropDown;
        FlagImageView imvFlag;
        TextView tvLangCode;
        TextView tvUserName;
        ImageView userIcon;
        ImageView imvAddPhrase;

        OneQuizTranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            phrasesLayout = itemView.findViewById(R.id.phrasesLayout);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvQuizDescription = itemView.findViewById(R.id.tvQuizDescription);
            imvDeleteTranslation = itemView.findViewById(R.id.imvDeleteTranslation);
            imvDropDown = itemView.findViewById(R.id.imvDropDown);
            imvFlag = itemView.findViewById(R.id.imvFlag);
            tvLangCode = itemView.findViewById(R.id.tvLangCode);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            userIcon = itemView.findViewById(R.id.userIcon);
            approveTranslation = itemView.findViewById(R.id.approveTranslation);
            imvAddPhrase = itemView.findViewById(R.id.imvAddPhrase);
        }
    }

    class PhraseViewHolder extends RecyclerView.ViewHolder {

        TextView tvPhraseText;
        ImageView userIcon;
        TextView tvUserName;
        Switch approvePhrase;
        ImageView imvDeletePhrase;

        PhraseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhraseText = itemView.findViewById(R.id.tvPhraseText);
            userIcon = itemView.findViewById(R.id.userIcon);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            approvePhrase = itemView.findViewById(R.id.approvePhrase);
            imvDeletePhrase = itemView.findViewById(R.id.imvDeletePhrase);
        }
    }
}
