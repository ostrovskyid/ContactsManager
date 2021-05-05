package org.example.contactsmanager.di

import dagger.Component
import org.example.contactsmanager.repository.ContactsRepository
import org.example.contactsmanager.view.screens.ContactDetailsView
import org.example.contactsmanager.view.screens.ContactsListView
import org.example.contactsmanager.vm.ContactsListVM
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(contactsRepository: ContactsRepository)

    fun inject(model: ContactsListVM)

    fun inject(mainView: ContactsListView)
    fun inject(view: ContactDetailsView)
}