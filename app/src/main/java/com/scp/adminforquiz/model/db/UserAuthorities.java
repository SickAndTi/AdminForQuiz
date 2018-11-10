package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserAuthorities {
    @PrimaryKey
    public Integer id;

    public String authority;
}
