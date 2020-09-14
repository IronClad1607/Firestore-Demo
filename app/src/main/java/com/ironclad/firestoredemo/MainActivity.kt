package com.ironclad.firestoredemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "PUI"
    private val keyTitle = "title"
    private val keyDescription = "description"

    private val db by lazy {
        Firebase.firestore
    }

    private val notebookRef = db.collection("Notebook")
    private val noteRef = db.collection("Notebook")
        .document("First Note")

    private lateinit var noteListener: ListenerRegistration

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()

            val message = Note(title, description)

            notebookRef.add(message)
        }

        btnLoad.setOnClickListener {
            notebookRef.get()
                .addOnSuccessListener {
                    var data = ""
                    for (value in it) {
                        val note = value.toObject(Note::class.java)
                        data += "Title: ${note.title} \n Description: ${note.description} \n\n"
                    }

                    tvShow.text = data
                }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
    }
}