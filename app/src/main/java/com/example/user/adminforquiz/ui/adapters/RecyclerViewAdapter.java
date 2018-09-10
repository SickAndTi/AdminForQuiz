package com.example.user.adminforquiz.ui.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.adminforquiz.GlideApp;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.api.NwQuiz;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NwQuiz> nwQuizList = new ArrayList<>();

    public List<NwQuiz> getNwQuizList() {
        return nwQuizList;
    }

    public void setNwQuizList(List<NwQuiz> nwQuizList) {
        this.nwQuizList = nwQuizList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        NwQuiz nwQuiz = nwQuizList.get(position);
        viewHolder.tvScpNumber.setText(nwQuiz.scpNumber);
        viewHolder.approved.setChecked(nwQuiz.approved);
        viewHolder.authorId.setText(Math.toIntExact(nwQuiz.authorId));
        viewHolder.approverId.setText(Math.toIntExact(nwQuiz.approverId));
        viewHolder.dateCreated.setText((CharSequence) nwQuiz.created);
        viewHolder.dateUpdated.setText((CharSequence) nwQuiz.updated);
        viewHolder.ruTranslation.setImageResource(R.drawable.rflag);
        viewHolder.enTranslation.setImageResource(R.drawable.gbrflag);
        viewHolder.btnAdd.setImageResource(R.drawable.btnadd);
        viewHolder.btnDelete.setImageResource(R.drawable.btndelete);
        GlideApp
                .with(holder.itemView.getContext())
                .load(nwQuiz.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return nwQuizList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        EditText tvScpNumber;
        Switch approved;
        ImageButton btnDelete, btnAdd, ruTranslation, enTranslation;
        ImageView imageView;
        TextView authorId, approverId, dateCreated, dateUpdated;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScpNumber = itemView.findViewById(R.id.tvScpNumber);
            approved = itemView.findViewById(R.id.approved);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ruTranslation = itemView.findViewById(R.id.ruTranslation);
            enTranslation = itemView.findViewById(R.id.enTranslation);
            imageView = itemView.findViewById(R.id.imageView);
            authorId = itemView.findViewById(R.id.authorId);
            approverId = itemView.findViewById(R.id.approverId);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            dateUpdated = itemView.findViewById(R.id.dateUpdated);
        }


    }
}
