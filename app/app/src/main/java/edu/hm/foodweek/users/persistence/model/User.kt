package edu.hm.foodweek.users.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var email: String,
    var username: String
)
