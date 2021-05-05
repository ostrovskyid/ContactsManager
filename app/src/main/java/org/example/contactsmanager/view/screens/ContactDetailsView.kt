package org.example.contactsmanager.view.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.example.contactsmanager.R
import org.example.contactsmanager.databinding.ActivityContactDetailsBinding
import org.example.contactsmanager.di.DaggerAppComponent
import org.example.contactsmanager.model.ContactUi
import org.example.contactsmanager.vm.ContactsListVM

class ContactDetailsView : AppCompatActivity() {
    companion object {
        const val KEY_CONTACT_ID = "contact_id"
        fun getIntent(context: Context, id: Int = -1) =
            Intent(context, ContactDetailsView::class.java).apply {
                putExtra(
                    KEY_CONTACT_ID, id
                )
            }
    }

    private val model: ContactsListVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView(
            this, R.layout.activity_contact_details
        ) as ActivityContactDetailsBinding
        binding.lifecycleOwner = this
        binding.viewmodel = model

        DaggerAppComponent.create().inject(this)

        Toast.makeText(this, "You can edit fields", Toast.LENGTH_LONG).show()

        model.fetchContactById(intent.getIntExtra(KEY_CONTACT_ID, -1))

        model.contactsRepository.contactUi.observe(this, {
            binding.contactUi = it
            model.saveClicked.observe(this, { savaClicked ->
                if (savaClicked) {
                    model.updateContact(
                        ContactUi(
                            id = intent.getIntExtra(KEY_CONTACT_ID, -1),
                            firsname = binding.tvName.text.toString(),
                            lastname = binding.tvLastName.text.toString(),
                            email = binding.tvEmail?.text.toString(),
                            image = it.image
                        )
                    )
                    startActivity(
                        Intent(
                            this,
                            ContactsListView::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }

            })
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_contact -> {
                model.deleteContact()
                startActivity(
                    Intent(
                        this,
                        ContactsListView::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}