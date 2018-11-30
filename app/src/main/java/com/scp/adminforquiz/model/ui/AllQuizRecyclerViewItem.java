package com.scp.adminforquiz.model.ui;

public class AllQuizRecyclerViewItem {
    public enum AllQuizRecyclerViewItemType {QUIZ, PROGRESSBAR}

    public Object data;
    public AllQuizRecyclerViewItemType type;

    public AllQuizRecyclerViewItem(Object data, AllQuizRecyclerViewItemType type) {
        this.data = data;
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public AllQuizRecyclerViewItemType getType() {
        return type;
    }
}
