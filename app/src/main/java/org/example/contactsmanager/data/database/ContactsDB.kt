package org.example.contactsmanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.contactsmanager.utils.DATABASE_NAME

@Database(entities = [ContactDBEntity::class], version = 1)
abstract class ContactsDB : RoomDatabase() {

    abstract fun dataDao(): ContactDAO

    companion object {

        @Volatile // All threads have immediate access to this property
        private var instance: ContactsDB? = null

        private val LOCK = Any() // Makes sure no threads making the same thing at the same time

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ContactsDB::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

}