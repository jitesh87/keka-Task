package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import android.view.View
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var addButton: Button
    private lateinit var listView: ListView

    private lateinit var items: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_text)
        addButton = findViewById(R.id.button)
        listView = findViewById(R.id.list)

        items = ArrayList()
        adapter = ArrayAdapter(this, R.layout.list_item_layout, R.id.item_text, items)
        listView.adapter = adapter

        addButton.setOnClickListener {
            addItem()
        }

        listView.setOnItemClickListener { _, view, position, _ ->
            showEditDialog(position)
        }

        listView.setOnItemLongClickListener { _, view, position, _ ->
            onDeleteClick(view.findViewById(R.id.delete_icon))
            true
        }
    }

    private fun addItem() {
        val newItem = editText.text.toString().trim()
        if (newItem.isNotEmpty()) {
            items.add(newItem)
            adapter.notifyDataSetChanged()
            editText.text.clear()
        }
    }

    private fun showEditDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.edit_dialog_layout, null)
        val editTextDialog = dialogView.findViewById<EditText>(R.id.edit_text_dialog)

        builder.setView(dialogView)
            .setTitle("Edit Item")
            .setPositiveButton("Save") { _, _ ->
                val editedText = editTextDialog.text.toString().trim()
                if (editedText.isNotEmpty()) {
                    items[position] = editedText
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        editTextDialog.setText(items[position])

        val dialog = builder.create()
        dialog.show()
    }

    fun onDeleteClick(view: View) {
        val position = listView.getPositionForView(view.parent as View)
        items.removeAt(position)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Item deleted at position $position", Toast.LENGTH_SHORT).show()
    }
}
