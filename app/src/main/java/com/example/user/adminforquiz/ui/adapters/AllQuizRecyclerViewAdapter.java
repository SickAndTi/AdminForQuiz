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

    public List<Quiz> getQuizList() {
        return quizList;
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
        viewHolder.dateCreated.setText(DateTypeConverter.formatDate(quiz.created));
        viewHolder.dateUpdated.setText(DateTypeConverter.formatDate(quiz.updated));
        viewHolder.ruTranslation.setImageResource(R.drawable.rflag);
        viewHolder.enTranslation.setImageResource(R.drawable.gbrflag);
        viewHolder.btnAdd.setImageResource(R.drawable.btnadd);
        viewHolder.btnDelete.setImageResource(R.drawable.btndelete);
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
        ImageView btnDelete, btnAdd, ruTranslation, enTranslation;
        ImageView imageView;
        TextView dateCreated, dateUpdated;


        QuizViewHolder(@NonNull View itemView) {
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
        }


    }

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }
}
