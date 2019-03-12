package com.scp.adminforquiz.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.haipq.android.flagkit.FlagImageView;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.di.GlideApp;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.ui.AllQuizRecyclerViewItem;
import com.scp.adminforquiz.util.DateTypeConverter;
import com.scp.adminforquiz.util.DimensionUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AllQuizRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllQuizRecyclerViewItem> allQuizRecyclerViewItemList = new ArrayList<>();
    private List<AllQuizRecyclerViewItem> filterList = new ArrayList<>();
    private OnQuizClickListener onQuizClickListener;

    public AllQuizRecyclerViewAdapter(OnQuizClickListener onQuizClickListener) {
        this.onQuizClickListener = onQuizClickListener;
    }

    public void setQuizList(List<Quiz> quizList) {
        allQuizRecyclerViewItemList.clear();
        filterList.clear();
        for (Quiz quiz : quizList) {
            allQuizRecyclerViewItemList.add(new AllQuizRecyclerViewItem(quiz, AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.QUIZ));
        }
        filterList.addAll(allQuizRecyclerViewItemList);
        notifyDataSetChanged();
    }

    public void filter(String queryText) {
        Timber.d("FILTER in ADAPTER : %s", queryText);
        filterList.clear();
        if (queryText.isEmpty()) {
            filterList.addAll(allQuizRecyclerViewItemList);
            Timber.d("FILTER in ADAPTER empty text : %s", queryText);
        } else {
            for (AllQuizRecyclerViewItem allQuizRecyclerViewItem : allQuizRecyclerViewItemList) {
                if (allQuizRecyclerViewItem.type == AllQuizRecyclerViewItem.AllQuizRecyclerViewItemType.QUIZ) {
                    Quiz quiz = (Quiz) (allQuizRecyclerViewItem.getData());
                    if (quiz.scpNumber.toLowerCase().contains(queryText.toLowerCase())) {
                        filterList.add(allQuizRecyclerViewItem);
                        Timber.d("FILTER in ADAPTER  not empty text : %s", queryText);
                    } else {
                        for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                            if (quizTranslation.translation.toLowerCase().contains(queryText.toLowerCase())) {
                                filterList.add(allQuizRecyclerViewItem);
                                Timber.d("FILTER in ADAPTER  not empty text : %s", queryText);
                            }
                        }
                    }
                } else {
                    filterList.add(allQuizRecyclerViewItem);
                }
            }
        }
        Timber.d("FILTER in ADAPTER  FINAL : %s", queryText);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_inlist, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuizViewHolder viewHolder = (QuizViewHolder) holder;
        Quiz quiz = (Quiz) filterList.get(position).data;
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
            String correctLangCode = quizTranslation.langCode;
            FlagImageView flagImage = new FlagImageView(viewHolder.flagLayout.getContext());
            if (quizTranslation.langCode.equals("en")) {
                correctLangCode = "gb";
            }
            flagImage.setCountryCode(correctLangCode);
            flagImage.setMaxWidth(DimensionUtils.convertDpToPixels(50));
            flagImage.setMaxHeight(DimensionUtils.convertDpToPixels(38));
            flagImage.setPadding(DimensionUtils.convertDpToPixels(6), DimensionUtils.convertDpToPixels(6), DimensionUtils.convertDpToPixels(6), DimensionUtils.convertDpToPixels(6));
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
        viewHolder.itemView.setOnClickListener(view ->
                onQuizClickListener.onQuizClick(quiz)
        );

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return filterList.get(position).type.ordinal();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView tvScpNumber;
        TextView tvTitle;
        Switch approved;
        ImageView imageView, userIcon;
        TextView dateCreated, dateUpdated;
        TextView tvDateCreated, tvDateUpdated;
        FlexboxLayout flagLayout;

        QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            approved = itemView.findViewById(R.id.approved);
            imageView = itemView.findViewById(R.id.imageView);
            userIcon = itemView.findViewById(R.id.userIcon);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            tvDateCreated = itemView.findViewById(R.id.tvDateCreated);
            tvDateUpdated = itemView.findViewById(R.id.tvDateUpdated);
            flagLayout = itemView.findViewById(R.id.flagLayout);
        }
    }

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }
}
