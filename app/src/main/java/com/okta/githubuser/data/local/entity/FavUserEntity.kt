package com.okta.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favUsers")
class FavUserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String = "",

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null
)