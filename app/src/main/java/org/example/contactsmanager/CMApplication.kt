package org.example.contactsmanager

import android.app.Application
import org.example.contactsmanager.data.database.ContactsDB

class CMApplication : Application() {

    companion object {
        lateinit var instance: CMApplication
        lateinit var database: ContactsDB
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = ContactsDB.invoke(this)
    }
}