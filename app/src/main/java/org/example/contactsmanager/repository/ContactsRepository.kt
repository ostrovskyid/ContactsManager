package org.example.contactsmanager.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import org.example.contactsmanager.CMApplication
import org.example.contactsmanager.data.database.*
import org.example.contactsmanager.data.network.RandomUserApi
import org.example.contactsmanager.di.DaggerAppComponent
import org.example.contactsmanager.model.ContactUi
import org.example.contactsmanager.model.ContactsResult
import javax.inject.Inject


class ContactsRepository {

    @Inject
    lateinit var randomUserApiService: RandomUserApi

    private val _data by lazy { MutableLiveData<List<ContactUi>>() }
    val contact: LiveData<List<ContactUi>>
        get() = _data

    private val _dataui by lazy { MutableLiveData<ContactUi>() }
    val contactUi: LiveData<ContactUi>
        get() = _dataui

    private val _isInProgress by lazy { MutableLiveData<Boolean>() }
    val isInProgress: LiveData<Boolean>
        get() = _isInProgress

    private val _isError by lazy { MutableLiveData<Boolean>() }
    val isError: LiveData<Boolean>
        get() = _isError


    init {
        DaggerAppComponent.create().inject(this)
    }

    private fun insertData(): Disposable {
        return randomUserApiService.getContacts()
            .subscribeOn(Schedulers.io())
            .subscribeWith(subscribeToDatabase())
    }

    private fun subscribeToDatabase(): DisposableSubscriber<ContactsResult> {
        return object : DisposableSubscriber<ContactsResult>() {

            override fun onNext(contactsResult: ContactsResult?) {
                if (contactsResult != null) {
                    Log.d("received users", "users: ${contactsResult.data}")
                    val entityList = contactsResult.data.toList().toDataEntityList()

                    CMApplication.database.apply {
                        dataDao().insertData(entityList)
                    }
                }
            }

            override fun onError(t: Throwable?) {
                _isInProgress.postValue(true)
                Log.e("insertData()", "ContactsResult error: ${t?.message}")
                _isError.postValue(true)
                _isInProgress.postValue(false)
            }

            override fun onComplete() {
                Log.v("insertData()", "insert success")
                getContactsQuery()
            }
        }
    }

    private fun getContactsQuery(): Disposable {
        return CMApplication.database.dataDao()
            .queryData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dataEntityList ->
                    _isInProgress.postValue(true)
                    if (dataEntityList != null && dataEntityList.isNotEmpty()) {
                        _isError.postValue(false)
                        _data.postValue(dataEntityList.toUiList())
                    } else {
                        insertData()
                    }
                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getTrendingQuery()", "Database error: ${it.message}")
                    _isError.postValue(true)
                    _isInProgress.postValue(false)
                }
            )
    }


    private fun deleteAllQuery(): Disposable {
        return CMApplication.database.dataDao()
            .deleteUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    insertData()
                },
                {
                    Log.e("deleteAllContacts", "Database error: ${it.message}")
                }

            )
    }

    private fun getContactByIdQuery(id: Int?): Disposable {
        return CMApplication.database.dataDao()
            .queryById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { item ->
                    _isInProgress.postValue(true)
                    _isError.postValue(false)
                    _dataui.postValue(item.toUi())
                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getTrendingQuery()", "Database error: ${it.message}")
                    _isError.postValue(true)
                    _isInProgress.postValue(false)
                }
            )
    }

    private fun deleteSelectedContanct(): Disposable {
        return CMApplication.database.dataDao()
            .deleteContact(_dataui.value?.toDataEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getContactsQuery()
                },
                {
                    Log.e("deleteSelectedContact", "Database error: ${it.message}")
                }

            )

    }

    private fun updateSelectedContact(contactDBEntity: ContactDBEntity): Disposable {
        return CMApplication.database.dataDao()
            .updateContact(contactDBEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getContactsQuery()
                },
                {
                    Log.e("updateSelectedContact", "Database error: ${it.message}")
                }

            )

    }


    fun fetchDataFromDatabase(): Disposable = getContactsQuery()
    fun deleteAllContactsInDB(): Disposable = deleteAllQuery()
    fun fetchItemById(id: Int?): Disposable = getContactByIdQuery(id)
    fun deleteContact(): Disposable = deleteSelectedContanct()
    fun updateContact(contactUi: ContactUi): Disposable =
        updateSelectedContact(contactUi.toDataEntity())


}

