package org.example.contactsmanager.data.network

import io.reactivex.Flowable
import org.example.contactsmanager.model.ContactsResult
import retrofit2.http.GET


interface RandomUserApi {

    @GET("?results=20")
    fun getContacts(): Flowable<ContactsResult>

}