package org.example.contactsmanager.data.database

import org.example.contactsmanager.model.Contact
import org.example.contactsmanager.model.ContactUi


fun ContactDBEntity.toUi() = ContactUi(
    id = this.id,
    firsname = this.firstname,
    lastname = this.lastname,
    email = this.email,
    image = this.largeimage
)

fun ContactUi.toDataEntity() = ContactDBEntity(
    id = this.id,
    firstname = this.firsname,
    lastname = this.lastname,
    email = this.email,
    largeimage = this.image
)

fun List<ContactDBEntity>.toUiList() = this.map { it.toUi() }
fun List<ContactUi>.toDbList() = this.map { it.toDataEntity() }

fun Contact.toDataEntity() = ContactDBEntity(
    firstname = this.name.first,
    lastname = this.name.last,
    email = this.email,
    largeimage = this.picture.large,
)

fun List<Contact>.toDataEntityList() = this.map { it.toDataEntity() }