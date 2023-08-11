package com.john_halaka.notes.ui.presentaion.notes_list

import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()

    data class DeleteNote(val note: Note) : NotesEvent()

    data class UpdateNote(val note: Note) : NotesEvent()

    object RestoreNote : NotesEvent()

    object ToggleOrderSection : NotesEvent()


    data class SearchNotes(val searchPhrase: String) : NotesEvent()
}
