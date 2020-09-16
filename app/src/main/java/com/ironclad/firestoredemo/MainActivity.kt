package com.ironclad.firestoredemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Tasks.whenAllSuccess
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
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

            if (etPriority.length() == 0) {
                etPriority.setText("0")
            }

            val priority = etPriority.text.toString().toInt()

            val message = Note(title, description, priority)

            notebookRef.add(message)
        }

        btnLoad.setOnClickListener {
            notebookRef.whereGreaterThanOrEqualTo("priority", 2)
                .whereEqualTo("title", "Aa")
                .orderBy("priority", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    var data = ""
                    for (value in it) {
                        val note = value.toObject(Note::class.java)
                        note.documentId = value.id

                        data += "ID: ${note.documentId} \n Title: ${note.title} \n Description: ${note.description} \n Priority: ${note.priority} \n\n"
                    }

                    tvShow.text = data
                }
                .addOnFailureListener {
                    Log.d(TAG, it.toString())
                }
        }

        btnLoadOr.setOnClickListener {
            val task1 = notebookRef.whereLessThan("priority", 2)
                .orderBy("priority")
                .get()

            val task2 = notebookRef.whereGreaterThan("priority", 2)
                .orderBy("priority")
                .get()

            val allTask: Task<List<QuerySnapshot>> = whenAllSuccess(task1, task2)
            allTask.addOnSuccessListener {
                var data = ""

                for (main in it) {
                    for (value in main) {
                        val note = value.toObject(Note::class.java)
                        note.documentId = value.id

                        data += "ID: ${note.documentId} \n Title: ${note.title} \n Description: ${note.description} \n Priority: ${note.priority} \n\n"
                    }
                }

                tvShow.text = data
            }

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        noteListener = notebookRef.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            var data = ""
            for (snapshot in value!!) {
                val note = snapshot.toObject(Note::class.java)
                note.documentId = snapshot.id

                data += "ID: ${note.documentId} \n Title: ${note.title} \n Description: ${note.description} \n Priority:${note.priority} \n\n"
            }

            tvShow.text = data
        }
    }

    override fun onStop() {
        noteListener.remove()
        super.onStop()
    }
}