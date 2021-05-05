package org.example.contactsmanager.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contact")
data class ContactDBEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "firstname")
    val firstname: String,
    @ColumnInfo(name = "lastnmae")
    val lastname: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "largeimage")
    val largeimage: String
)


