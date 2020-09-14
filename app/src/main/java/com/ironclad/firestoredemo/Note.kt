package com.ironclad.firestoredemo

import com.google.firebase.firestore.Exclude

class Note {
    var title: String = ""
    var description: String = ""

    @get:Exclude
    var documentId: String = ""

    var priority: Int = 0

    constructor() {
    }

    constructor(title: String, description: String, priority: Int) {
        this.title = title
        this.description = description
        this.priority = priority
    }
}