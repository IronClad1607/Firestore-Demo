package com.ironclad.firestoredemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var noteListener: ListenerRegistration

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()

            val message = hashMapOf(
                keyTitle to title,
                keyDescription to description
            )

            db.collection("Notebook")
                .document("First Note")
                .set(message)
                .addOnSuccessListener {
                    Log.d(TAG, "Document added with id:$it.id")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Document Failed ${it.stackTrace}")
                }
        }

        btnLoad.setOnClickListener {
            db.collection("Notebook")
                .document("First Note")
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val title = it.getString(keyTitle)
                        val description = it.getString(keyDescription)

                        tvShow.text = "Title: $title \n Description: $description"
                    } else {
                        Log.d(TAG, "Document doesn't exist!")
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Document failed: ${it.toString()}")
                }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        noteListener = db.collection("Notebook")
            .document("First Note").addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(TAG, "Document error: ${error.toString()}")
                }

                if (value!!.exists()) {
                    val title = value.getString(keyTitle)
                    val description = value.getString(keyDescription)

                    tvShow.text = "Title: $title \n Description: $description"
                }
            }
    }
}