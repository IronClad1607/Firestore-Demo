package com.ironclad.firestoredemo

import com.google.firebase.firestore.Exclude

class Note {
    var title: String = ""
    var description: String = ""

    @Exclude
    var documentId: String = ""

    constructor() {
    }

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }
}