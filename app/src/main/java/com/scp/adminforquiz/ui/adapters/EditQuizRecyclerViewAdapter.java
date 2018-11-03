package com.scp.adminforquiz.ui.adapters;

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
    private EditInterface editInterface;

    public interface EditInterface {
        void onTranslationEditClicked(QuizTranslation quizTranslation);

        void onTranslationDeleteClicked(QuizTranslation quizTranslation);

        void onTranslationPhraseDeleteClicked(QuizTranslationPhrase quizTranslationPhrase);

        void onTranslationAddPhraseClicked(QuizTranslation quizTranslation);

        void onApproveQuizClicked(Quiz quiz);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (oneQuizRecyclerViewItemList.get(position).type) {
            case QUIZ:
                EditOneQuizViewHolder viewHolder = (EditOneQuizViewHolder) holder;
                Quiz quiz = (Quiz) oneQuizRecyclerViewItemList.get(position).data;
                viewHolder.approved.setChecked(quiz.approved);
                viewHolder.approved.setOnCheckedChangeListener((buttonView, isChecked) -> editInterface.onApproveQuizClicked(quiz));
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
                editOneQuizTranslationViewHolder.btnDeleteTranslation.setOnClickListener(v -> editInterface.onTranslationDeleteClicked(quizTranslation));
                editOneQuizTranslationViewHolder.etPhrasesLayout.removeAllViews();
                for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                    TextView textView = new TextView(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    textView.setText(quizTranslationPhrase.translation);
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(textView);
                    Button btnDeleteTranslationPhrase = new Button(editOneQuizTranslationViewHolder.etPhrasesLayout.getContext());
                    btnDeleteTranslationPhrase.setText(R.string.deleteTranslationPhrase);
                    btnDeleteTranslationPhrase.setOnClickListener(v -> editInterface.onTranslationPhraseDeleteClicked(quizTranslationPhrase));
                    editOneQuizTranslationViewHolder.etPhrasesLayout.addView(btnDeleteTranslationPhrase);
                }
                editOneQuizTranslationViewHolder.tvQuizDescription.setOnClickListener(v -> editInterface.onTranslationEditClicked(quizTranslation));
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
        Button btnDeleteTranslation;

        EditOneQuizTranslationViewHolder(@NonNull View itemView) {
            super(itemView);
            etPhrasesLayout = itemView.findViewById(R.id.etPhrasesLayout);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvQuizDescription = itemView.findViewById(R.id.tvQuizDescription);
            btnDeleteTranslation = itemView.findViewById(R.id.btnDeleteTranslation);
        }
    }
}
