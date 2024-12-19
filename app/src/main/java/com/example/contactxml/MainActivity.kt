package com.example.contactxml

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Contact(val name: String, val phone: String)

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onDeleteClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: android.widget.TextView = itemView.findViewById(R.id.nameTextView)
        val phoneTextView: android.widget.TextView = itemView.findViewById(R.id.phoneTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ContactViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phone

        holder.deleteButton.setOnClickListener {
            onDeleteClick(contact)
        }
    }

    override fun getItemCount() = contacts.size
}

class MainActivity : AppCompatActivity() {
    private lateinit var contactRecyclerView: RecyclerView
    private lateinit var addContactButton: Button

    private val contactsList = mutableListOf<Contact>()
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactRecyclerView = findViewById(R.id.contactRecyclerView)
        addContactButton = findViewById(R.id.addContactButton)

        contactAdapter = ContactAdapter(contactsList) { contact ->
            val index = contactsList.indexOf(contact)
            if (index != -1) {
                contactsList.removeAt(index)
                contactAdapter.notifyItemRemoved(index)
                Toast.makeText(this, "Контакт удален", Toast.LENGTH_SHORT).show()
            }
        }

        contactRecyclerView.layoutManager = LinearLayoutManager(this)
        contactRecyclerView.adapter = contactAdapter

        addContactButton.setOnClickListener {
            showAddContactDialog()
        }
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.nameEditText)
        val phoneEditText: EditText = dialogView.findViewById(R.id.phoneEditText)

        AlertDialog.Builder(this)
            .setTitle("Добавить контакт")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = nameEditText.text.toString().trim()
                val phone = phoneEditText.text.toString().trim()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    val newContact = Contact(name, phone)
                    contactsList.add(newContact)
                    contactAdapter.notifyItemInserted(contactsList.size - 1)
                    Toast.makeText(this, "Контакт добавлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}