package com.hfad.notes

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    companion object {
        lateinit var titles : ArrayList<String>
        lateinit var descriptions : ArrayList<String>
        lateinit var adapter : ArrayAdapter<String>
    }

    lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences("com.hfad.notes", Context.MODE_PRIVATE)

        titles = ArrayList<String>()
        descriptions = ArrayList<String>()
        titles.clear()
        descriptions.clear()

        try {
            titles = ObjectSerializer.deserialize(sharedPreferences.getString("titles", ObjectSerializer.serialize(object : ArrayList<String>(){}))) as ArrayList<String>
            descriptions = ObjectSerializer.deserialize(sharedPreferences.getString("descriptions", ObjectSerializer.serialize(object : ArrayList<String>(){}))) as ArrayList<String>
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        listView_notes.adapter = adapter
        listView_notes.setOnItemClickListener(this)
        listView_notes.setOnItemLongClickListener(this)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Delete Note")
                .setMessage("Do you want to delete selected note?")
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        titles.removeAt(position)
                        descriptions.removeAt(position)
                        sharedPreferences.edit().putString("titles", ObjectSerializer.serialize(titles)).apply()
                        sharedPreferences.edit().putString("descriptions", ObjectSerializer.serialize(descriptions)).apply()
                        adapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Deleted Sucessfully!", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", null)
                .show()
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(applicationContext, AddNewNote::class.java)
        intent.putExtra("position", position)
        intent.putExtra("title", titles.get(position))
        intent.putExtra("description", descriptions.get(position))
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when(item!!.itemId){
            R.id.add_note -> {
                val intent = Intent(applicationContext, AddNewNote::class.java)
                intent.putExtra("title", "new")
                intent.putExtra("description", "new")
                startActivity(intent)
                return true
            }
        }
        return false
    }
}
