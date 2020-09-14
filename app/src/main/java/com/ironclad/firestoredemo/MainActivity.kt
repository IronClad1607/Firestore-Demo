package com.ironclad.firestoredemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "PUI"
    private val keyTitle = "title"
    private val keyDescription = "description"

    val db by lazy {
        Firebase.firestore
    }

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
    }
}