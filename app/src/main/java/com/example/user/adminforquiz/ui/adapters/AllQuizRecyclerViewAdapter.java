package com.example.user.adminforquiz.ui.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.di.GlideApp;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.util.DateTypeConverter;

import java.util.ArrayList;
import java.util.List;

public class AllQuizRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Quiz> quizList = new ArrayList<>();
    private OnQuizClickListener onQuizClickListener;

    public AllQuizRecyclerViewAdapter(OnQuizClickListener onQuizClickListener) {
        this.onQuizClickListener = onQuizClickListener;

    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_inlist, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuizViewHolder viewHolder = (QuizViewHolder) holder;
        Quiz quiz = quizList.get(position);
        viewHolder.tvScpNumber.setText(quiz.scpNumber);
        viewHolder.approved.setChecked(quiz.approved);
        viewHolder.approved.setClickable(false);
        viewHolder.dateCreated.setText(DateTypeConverter.formatDate(quiz.created));
        viewHolder.dateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
        GlideApp
                .with(holder.itemView.getContext())
                .load(quiz.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.imageView);
        viewHolder.itemView.setOnClickListener(view ->
                onQuizClickListener.onQuizClick(quiz)
        );
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView tvScpNumber;
        Switch approved;
        ImageView imageView;
        TextView dateCreated, dateUpdated;
        TextView tvDateCreated, tvDateUpdated;

        QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            approved = itemView.findViewById(R.id.approved);
            imageView = itemView.findViewById(R.id.imageView);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
            tvDateCreated = itemView.findViewById(R.id.tvDateCreated);
            tvDateUpdated = itemView.findViewById(R.id.tvDateUpdated);
        }
    }

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }
}
