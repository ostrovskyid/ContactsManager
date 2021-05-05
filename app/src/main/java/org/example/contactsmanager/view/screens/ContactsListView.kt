package org.example.contactsmanager.view.screens

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.example.contactsmanager.R
import org.example.contactsmanager.databinding.ActivityMainBinding
import org.example.contactsmanager.di.DaggerAppComponent
import org.example.contactsmanager.view.adapter.ContactsAdapter
import org.example.contactsmanager.vm.ContactsListVM
import javax.inject.Inject


class ContactsListView : AppCompatActivity() {

    @Inject
    lateinit var contactsAdapter: ContactsAdapter

    private val model: ContactsListVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        ) as ActivityMainBinding
        binding.lifecycleOwner = this
        binding.viewmodel = model

        DaggerAppComponent.create().inject(this)

        setUpRecyclerView()

        observeLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contacts_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_and_fetch -> {

                model.deleteAllContacts()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setUpRecyclerView() {
        recycler_view.apply {
            setHasFixedSize(true)
            adapter = contactsAdapter
        }
    }

    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeContactsList()
        observeClickedContact()
    }

    private fun observeClickedContact() {
        model.clickedContact.observe(this, {
            startActivity(ContactDetailsView.getIntent(this, it.id))
        })
    }

    private fun observeInProgress() {
        model.contactsRepository.isInProgress.observe(this, { isLoading ->
            isLoading.let {
                if (it) {
                    empty_text.visibility = View.GONE
                    recycler_view.visibility = View.GONE
                    fetch_progress.visibility = View.VISIBLE
                } else {
                    fetch_progress.visibility = View.GONE
                }
            }
        })
    }

    private fun observeIsError() {
        model.contactsRepository.isError.observe(this, { isError ->
            isError.let {
                if (it) {
                    disableViewsOnError()
                } else {
                    empty_text.visibility = View.GONE
                    fetch_progress.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun disableViewsOnError() {
        fetch_progress.visibility = View.VISIBLE
        empty_text.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
        contactsAdapter.setUpData(emptyList())
        fetch_progress.visibility = View.GONE
    }

    private fun observeContactsList() {
        model.contactsRepository.contact.observe(this, { contacts ->
            contacts.let {
                if (it != null && it.isNotEmpty()) {
                    fetch_progress.visibility = View.VISIBLE
                    recycler_view.visibility = View.VISIBLE
                    contactsAdapter.setUpVM(model)
                    contactsAdapter.setUpData(it)
                    empty_text.visibility = View.GONE
                    fetch_progress.visibility = View.GONE
                } else {
                    disableViewsOnError()
                }
            }
        })
    }

}
