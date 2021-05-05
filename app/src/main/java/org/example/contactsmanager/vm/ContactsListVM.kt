package org.example.contactsmanager.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.example.contactsmanager.di.DaggerAppComponent
import org.example.contactsmanager.model.ContactUi
import org.example.contactsmanager.repository.ContactsRepository
import javax.inject.Inject

class ContactsListVM : ViewModel() {

    @Inject
    lateinit var contactsRepository: ContactsRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _clickedContact by lazy { MutableLiveData<ContactUi>() }
    val clickedContact: LiveData<ContactUi>
        get() = _clickedContact


    private var _saveClicked = MutableLiveData<Boolean>()
    val saveClicked: LiveData<Boolean>
        get() = _saveClicked


    init {
        DaggerAppComponent.create().inject(this)
        compositeDisposable.add(contactsRepository.fetchDataFromDatabase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun onClickedContact(contact: ContactUi) {
        _clickedContact.value = contact
    }

    fun onSaveClicked(contactUi: ContactUi) {
        Log.d("saveclicktest", "save clicked!!!!!")
        _saveClicked.value = true
    }

    fun fetchContactById(id: Int) {
        contactsRepository.fetchItemById(id).let { compositeDisposable.add(it) }
    }

    fun deleteAllContacts() {
        contactsRepository.deleteAllContactsInDB().let { compositeDisposable.add(it) }
    }

    fun updateContact(contactUi: ContactUi) {
        contactsRepository.updateContact(contactUi).let { compositeDisposable.add(it) }
    }

    fun deleteContact() {
        contactsRepository.deleteContact().let { compositeDisposable.add(it) }
    }
}

