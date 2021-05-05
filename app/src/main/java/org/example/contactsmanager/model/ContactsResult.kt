package org.example.contactsmanager.model

import com.squareup.moshi.Json


data class ContactsResult(
    @field:Json(name = "results")
    val data: List<Contact>
)