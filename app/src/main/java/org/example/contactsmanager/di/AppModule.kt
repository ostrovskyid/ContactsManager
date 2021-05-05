package org.example.contactsmanager.di

import dagger.Module
import dagger.Provides
import org.example.contactsmanager.data.network.RandomUserApi
import org.example.contactsmanager.data.network.RandomUserApiService
import org.example.contactsmanager.model.ContactUi
import org.example.contactsmanager.repository.ContactsRepository
import org.example.contactsmanager.view.adapter.ContactsAdapter
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApi(): RandomUserApi = RandomUserApiService.getClient()

    @Provides
    fun provideContacsRepository() = ContactsRepository()


    @Provides
    fun provideListData() = ArrayList<ContactUi>()

    @Provides
    fun provideAdapter(data: ArrayList<ContactUi>): ContactsAdapter = ContactsAdapter(data)
}