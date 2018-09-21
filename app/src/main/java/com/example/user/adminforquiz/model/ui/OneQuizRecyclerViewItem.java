package com.example.user.adminforquiz.model.ui;

public class OneQuizRecyclerViewItem {
    public enum RecyclerAdapterItemType {QUIZ, QUIZ_TRANSLATION}

    public Object data;
    public RecyclerAdapterItemType type;

    public OneQuizRecyclerViewItem(Object data, RecyclerAdapterItemType type) {
        this.data = data;
        this.type = type;
    }
}
