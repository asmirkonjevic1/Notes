package com.hfad.notes

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_new_note.*

class AddNewNote : AppCompatActivity() {

    var isNoteNew : Boolean = false
    var position : Int = 0

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_note)

        sharedPreferences = this.getSharedPreferences("com.hfad.notes", Context.MODE_PRIVATE)

        if (intent.getStringExtra("title") != "new" && intent.getStringExtra("description") != "new"){
            position = intent.getIntExtra("position", 0)
            et_title.setText(intent.getStringExtra("title"), TextView.BufferType.EDITABLE)
            et_description.setText(intent.getStringExtra("description"), TextView.BufferType.EDITABLE)
            isNoteNew = false
        } else {
            isNoteNew = true
        }
    }

    fun saveNote(view : View){
        var message = ""

        if (et_title.text.toString() != "" && et_description.text.toString() != ""){
            if (isNoteNew) {
                MainActivity.titles.add(et_title.text.toString())
                MainActivity.descriptions.add(et_description.text.toString())
            }else{
                MainActivity.titles.set(position, et_title.text.toString())
                MainActivity.descriptions.set(position, et_description.text.toString())
            }
            sharedPreferences.edit().putString("titles", ObjectSerializer.serialize(MainActivity.titles)).apply()
            sharedPreferences.edit().putString("descriptions", ObjectSerializer.serialize(MainActivity.descriptions)).apply()
            MainActivity.adapter.notifyDataSetChanged()
            message = "Note Saved!"
            finish()
        }else {
            message = "Title or Description cannot be empty"
        }
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
