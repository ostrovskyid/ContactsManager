package org.example.contactsmanager.model

data class Contact(
    var name: NameData = NameData(),
    var email: String = "",
    var picture: PictureData = PictureData()
)
