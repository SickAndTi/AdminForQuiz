package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    //id = quiz.authorId
    @PrimaryKey
    public Long id;
    //content
    public String fullName;
    public String avatar;
}
