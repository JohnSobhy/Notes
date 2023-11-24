package com.john_halaka.noteTopia.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.john_halaka.noteTopia.feature_daily_quote.data.repository.QuoteRepositoryImpl
import com.john_halaka.noteTopia.feature_daily_quote.domain.repository.QuoteRepository
import com.john_halaka.noteTopia.feature_note.data.PreferencesManager


import com.john_halaka.noteTopia.feature_note.data.data_source.NoteDatabase
import com.john_halaka.noteTopia.feature_note.data.repository.NoteRepositoryImpl
import com.john_halaka.noteTopia.feature_note.domain.repository.NoteRepository
import com.john_halaka.noteTopia.feature_note.domain.use_case.AddNote
import com.john_halaka.noteTopia.feature_note.domain.use_case.DeleteNote
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetDeletedNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetFavouriteNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetNoteById
import com.john_halaka.noteTopia.feature_note.domain.use_case.GetNotes
import com.john_halaka.noteTopia.feature_note.domain.use_case.MoveNoteToTrash
import com.john_halaka.noteTopia.feature_note.domain.use_case.NoteUseCases
import com.john_halaka.noteTopia.feature_note.domain.use_case.UpdateNote
import com.john_halaka.noteTopia.feature_todo.data.repository.TodoRepositoryImpl
import com.john_halaka.noteTopia.feature_todo.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository, context: Context): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNotes = DeleteNote(repository),
            addNote = AddNote(repository,context),
            getNoteById = GetNoteById(repository),
            getFavouriteNotes = GetFavouriteNotes(repository),
            updateNote = UpdateNote(repository),
            moveNoteToTrash = MoveNoteToTrash(repository),
            getDeletedNotes = GetDeletedNotes(repository)

        )
    }


    @Provides
    @Singleton
    fun provideTodoRepository(db: NoteDatabase): TodoRepository {
        return TodoRepositoryImpl(db.todoDao)
    }


    @Provides
    @Singleton
    fun provideQuoteRepository(): QuoteRepository {
        return QuoteRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

//    @Provides
//    @Singleton
//    fun provideContext (@ApplicationContext context: Context) : Context {
//        return context
//    }

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context = application.applicationContext
}