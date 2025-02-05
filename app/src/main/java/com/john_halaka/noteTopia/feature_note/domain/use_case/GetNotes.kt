package com.john_halaka.noteTopia.feature_note.domain.use_case

import android.util.Log
import com.john_halaka.noteTopia.feature_note.domain.model.Note
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import com.john_halaka.noteTopia.feature_note.domain.util.NoteOrder
import com.john_halaka.noteTopia.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        Log.d("getNotes", "GetNotes useCase is invoked")
        return repository.getNotes().map { notes ->
            val (pinnedNotes, unpinnedNotes) = notes.partition { it.isPinned }
            val sortedPinnedNotes = sortNotes(pinnedNotes, noteOrder)
            val sortedUnpinnedNotes = sortNotes(unpinnedNotes, noteOrder)
            sortedPinnedNotes + sortedUnpinnedNotes
        }
    }

    companion object {
        fun sortNotes(notes: List<Note>, noteOrder: NoteOrder): List<Note> {
            return when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }

                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }

                    }
                }
            }
        }
    }
}
