package developer.abdusamid.notesapp.db.models

class Note {
    var noteId: Int? = null
    var noteName: String? = null
    var noteDescription: String? = null

    constructor(noteId: Int?, noteName: String?, noteDescription: String?) {
        this.noteId = noteId
        this.noteName = noteName
        this.noteDescription = noteDescription
    }
}