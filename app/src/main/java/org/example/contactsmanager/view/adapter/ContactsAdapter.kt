package org.example.contactsmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import org.example.contactsmanager.R
import org.example.contactsmanager.databinding.ItemContactBinding
import org.example.contactsmanager.model.ContactUi
import org.example.contactsmanager.vm.ContactsListVM


class ContactsAdapter(
    private val data: ArrayList<ContactUi>

) : RecyclerView.Adapter<ContactViewHolder>() {

    lateinit var vm: ViewModel

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemContactBinding: ItemContactBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_contact,
            parent,
            false
        )
        return ContactViewHolder(itemContactBinding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(data[position], vm as ContactsListVM)

    }

    fun setUpVM(vm: ViewModel) {
        this.vm = vm
    }

    fun setUpData(list: List<ContactUi>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}


class ContactViewHolder(
    val itemContactBinding: ItemContactBinding
) : RecyclerView.ViewHolder(itemContactBinding.root) {
    fun bind(contact: ContactUi, vm: ContactsListVM) {
        itemContactBinding.contact = contact
        itemContactBinding.vm = vm
        itemContactBinding.executePendingBindings()
    }
}