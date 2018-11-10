package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.scp.adminforquiz.model.api.NwUserAuthorities;

import java.util.List;

@Entity
public class User {
    //id = quiz.authorId
    @PrimaryKey
    public Long id;
    //content
    public String fullName;
    public String avatar;
    public List<UserAuthorities> authorities;

}
