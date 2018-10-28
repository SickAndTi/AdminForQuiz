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
import com.example.user.adminforquiz.model.ui.AllQuizRecyclerViewItem;
import com.example.user.adminforquiz.util.DateTypeConverter;
import com.haipq.android.flagkit.FlagImageView;

import java.util.ArrayList;
import java.util.List;

public class AllQuizRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllQuizRecyclerViewItem> allQuizRecyclerViewItemList = new ArrayList<>();
    private OnQuizClickListener onQuizClickListener;

    public AllQuizRecyclerViewAdapter(OnQuizClickListener onQuizClickListener) {
        this.onQuizClickListener = onQuizClickListener;
    }

    public void setQuizList(List<Quiz> quizList) {
        allQuizRecyclerViewItemList.clear();
        for (Quiz quiz : quizList) {
            allQuizRecyclerViewItemList.add(new AllQuizRecyclerViewItem(quiz, AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.QUIZ));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.values()[viewType]) {
            case QUIZ:
                return new QuizViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_inlist, parent, false));
            case PROGRESSBAR:
                return new ProgressBarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false));
            default:
                throw new IllegalArgumentException("Unexpected view in Adapter");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (allQuizRecyclerViewItemList.get(position).type) {
            case QUIZ:
                QuizViewHolder viewHolder = (QuizViewHolder) holder;
                Quiz quiz = (Quiz) allQuizRecyclerViewItemList.get(position).data;
                viewHolder.tvScpNumber.setText(quiz.scpNumber);
                for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                    viewHolder.tvTitle.setText(quizTranslation.translation);
                }
                viewHolder.approved.setChecked(quiz.approved);
                viewHolder.approved.setClickable(false);
                viewHolder.dateCreated.setText(DateTypeConverter.formatDate(quiz.created));
                viewHolder.dateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
                viewHolder.flagLayout.removeAllViews();
                for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                    FlagImageView flagImage = new FlagImageView(viewHolder.flagLayout.getContext());
                    if (quizTranslation.langCode.contains("en")) {
                        flagImage.setCountryCode("gb");
                        flagImage.setMaxWidth(50);
                        flagImage.setMaxHeight(38);
                        flagImage.setPadding(6, 6, 6, 6);
                        viewHolder.flagLayout.addView(flagImage);
                    } else if (quizTranslation.langCode.contains("ru")) {
                        flagImage.setCountryCode("ru");
                        flagImage.setMaxWidth(50);
                        flagImage.setMaxHeight(38);
                        flagImage.setPadding(6, 6, 6, 6);
                        viewHolder.flagLayout.addView(flagImage);
                    }
                }
                GlideApp
                        .with(holder.itemView.getContext())
                        .load(quiz.imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(viewHolder.imageView);
                viewHolder.itemView.setOnClickListener(view ->
                        onQuizClickListener.onQuizClick(quiz)
                );
                break;
            case PROGRESSBAR:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return allQuizRecyclerViewItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return allQuizRecyclerViewItemList.get(position).type.ordinal();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView tvScpNumber;
        TextView tvTitle;
        Switch approved;
        ImageView imageView;
        TextView dateCreated, dateUpdated;
        TextView tvDateCreated, tvDateUpdated;
        LinearLayout flagLayout;

        QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            approved = itemView.findViewById(R.id.approved);
            imageView = itemView.findViewById(R.id.imageView);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            tvDateCreated = itemView.findViewById(R.id.tvDateCreated);
            tvDateUpdated = itemView.findViewById(R.id.tvDateUpdated);
            flagLayout = itemView.findViewById(R.id.flagLayout);
        }
    }

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBarViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    public void showBottomProgress(boolean show) {
        if (show) {
            if (allQuizRecyclerViewItemList.get(allQuizRecyclerViewItemList.size() - 1).type != AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.PROGRESSBAR) {
                allQuizRecyclerViewItemList.add(new AllQuizRecyclerViewItem(null, AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.PROGRESSBAR));
            }
        } else {
            if (allQuizRecyclerViewItemList.get(allQuizRecyclerViewItemList.size() - 1).type == AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.PROGRESSBAR) {
                allQuizRecyclerViewItemList.remove(allQuizRecyclerViewItemList.size() - 1);
            }
        }
        notifyDataSetChanged();
    }
}
