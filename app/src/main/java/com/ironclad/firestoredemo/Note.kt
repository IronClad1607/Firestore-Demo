package com.ironclad.firestoredemo

class Note {
    var title: String = ""
    var description: String = ""

    constructor() {
    }

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }
}