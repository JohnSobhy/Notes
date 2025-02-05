package com.john_halaka.noteTopia.ui.presentaion.fav_notes

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.noteTopia.NavigationItemsBar
import com.john_halaka.noteTopia.R
import com.john_halaka.noteTopia.feature_note.data.PreferencesManager
import com.john_halaka.noteTopia.feature_note.domain.util.ViewType
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesEvent
import com.john_halaka.noteTopia.ui.presentaion.notes_list.NotesViewModel
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.GridViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.ListViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.NotesViewDropDownMenu
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SmallGridViewNotes
import com.john_halaka.noteTopia.ui.presentaion.notes_list.components.SortDropDownMenu
import com.john_halaka.noteTopia.ui.theme.BabyBlue
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavNotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    Log.d("favNotesScreen", "state: $state")
    val notesList = state.favouriteNotes
    val preferencesManager = remember { PreferencesManager(context) }
    var currentViewType by remember {
        mutableStateOf(
            ViewType.valueOf(
                preferencesManager.getString(
                    "viewPreference",
                    ViewType.GRID.name
                )
            )
        )
    }
    LaunchedEffect(currentViewType) {
        preferencesManager.saveString("viewPreference", currentViewType.name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.favorites))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    })
                    {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {

                    var expanded by remember { mutableStateOf(false) }
                    Box {

                        IconButton(
                            onClick = {
                                viewModel.onEvent(NotesEvent.ToggleOrderSection)
                                expanded = !expanded
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_sort_24),
                                contentDescription = stringResource(R.string.sort_notes)
                            )
                        }
                        SortDropDownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            noteOrder = state.noteOrder,
                            onOrderChange = {
                                viewModel.onEvent(NotesEvent.Order(it))
                            }
                        )

                    }
                    var viewMenuExpanded by remember { mutableStateOf(false) }

                    Box {

                        IconButton(
                            onClick = {
                                viewMenuExpanded = !viewMenuExpanded
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Change Notes view"
                            )
                        }
                        NotesViewDropDownMenu(
                            viewMenuExpanded = viewMenuExpanded,
                            onDismiss = { viewMenuExpanded = false },
                            viewType = currentViewType,
                            onViewChanged = {
                                currentViewType = it
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationItemsBar(navController = navController)
        },

        ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)

        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (state.favouriteNotes.isEmpty()) {
                var showProgress by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    showProgress = false
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (showProgress) {
                        CircularProgressIndicator(color = BabyBlue)
                    } else
                        Text(text = stringResource(R.string.you_have_no_favorite_notes))
                }
            } else {
                when (currentViewType) {
                    ViewType.GRID -> GridViewNotes(
                        navController = navController,
                        viewModel = viewModel,
                        notesList = notesList,
                        context = context,
                        showFavoriteIcon = true

                    )

                    ViewType.LIST -> ListViewNotes(
                        navController = navController,
                        viewModel = viewModel,
                        notesList = notesList,
                        context = context,
                        showFavoriteIcon = true
                    )

                    ViewType.SMALL_GRID -> SmallGridViewNotes(
                        navController = navController,
                        viewModel = viewModel,
                        notesList = notesList,
                        context = context,
                        showFavoriteIcon = true

                    )
                }
            }
        }
    }
}





