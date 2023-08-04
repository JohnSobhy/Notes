package com.john_halaka.notes.ui.presentaion.notes_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.john_halaka.notes.feature_note.domain.model.Note
import com.john_halaka.notes.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.notes.feature_note.domain.util.NoteOrder
import com.john_halaka.notes.feature_note.domain.util.OrderType
import com.john_halaka.notes.feature_note.domain.util.notesSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent){
        when (event) {
            is NotesEvent.Order ->{
                if (state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)

            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNotes (event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.SearchNotes -> {
                _state.value = state.value.copy(
                    searchResult = notesSearch(state.value.notes, event.searchPhrase )
                )

            }
        }

    }

    private fun getNotes(noteOrder: NoteOrder){

        getNotesJob?.cancel()

       getNotesJob= noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder= noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}