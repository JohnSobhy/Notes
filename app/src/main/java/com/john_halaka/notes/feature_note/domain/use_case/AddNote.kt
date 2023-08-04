package com.john_halaka.notes.feature_note.domain.use_case

import com.john_halaka.notes.feature_note.domain.model.InvalidNoteException
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.repository.NoteRepository

class AddNote (
    private val repository:NoteRepository
        ) {

    @Throws (InvalidNoteException::class)
    suspend operator fun invoke( note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title cannot be empty")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note cannot be empty")
        }
        repository.insertNote(note)
    }
}