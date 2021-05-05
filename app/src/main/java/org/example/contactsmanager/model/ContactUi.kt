package org.example.contactsmanager.model

data class ContactUi(
    var id: Int,
    var firsname: String,
    var lastname: String,
    var email: String,
    var image: String
) {
    fun fullName(): String = "$firsname $lastname"
}
