package org.example.contactsmanager.data.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: List<ContactDBEntity>)

    @Query("SELECT * from contact")
    fun queryData(): Single<List<ContactDBEntity>>

    @Query("SELECT * from contact WHERE id=:id")
    fun queryById(id: Int?): Single<ContactDBEntity>

    @Query("DELETE FROM contact")
    fun deleteUsers(): Completable

    @Delete
    fun deleteContact(contactDBEntity: ContactDBEntity?): Completable

    @Update
    fun updateContact(contactDBEntity: ContactDBEntity?): Completable
}