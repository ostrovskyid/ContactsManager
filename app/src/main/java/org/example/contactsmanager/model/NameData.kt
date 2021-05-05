package org.example.contactsmanager.model

data class NameData(
    var first: String = "",
    var last: String = ""
) {
    fun fullName(): String = "$first $last"
}