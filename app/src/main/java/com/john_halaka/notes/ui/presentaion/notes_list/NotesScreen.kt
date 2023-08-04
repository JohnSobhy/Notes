package com.john_halaka.notes.ui.presentaion.notes_list

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.john_halaka.notes.R

import com.john_halaka.notes.feature_note.domain.util.ViewType

import com.john_halaka.notes.ui.presentaion.notes_list.components.GridViewNotes
import com.john_halaka.notes.ui.presentaion.notes_list.components.ListViewNotes

import com.john_halaka.notes.ui.presentaion.notes_list.components.OrderSection
import com.john_halaka.notes.ui.Screen

import com.john_halaka.notes.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen (
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
//    var notesList by remember { mutableStateOf(state.notes) }
    var currentViewType by remember { mutableStateOf(ViewType.GRID) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                Modifier.background(color = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer ,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer

                ),
                title = {
                    Text(text = "Notes", style = Typography.headlineLarge)
                },
                navigationIcon = {
                    IconButton(onClick = {

                    })
                    {
                        Icon(Icons.Filled.Menu, contentDescription = "menu")
                    }

                },
                actions = {

                    IconButton(
                        onClick = {
                            viewModel.onEvent(NotesEvent.ToggleOrderSection)
                        },
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_sort_24),
                            contentDescription = "Sort notes"
                        )

                    }
                }
            )

            BoxWithConstraints {
                val boxWidth = maxWidth * 0.7f
                val iconWidth = (maxWidth - boxWidth) / 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = {
                            navController.navigate(Screen.NotesSearchScreen.route)
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "favorite notes")
                    }

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "favorite notes")
                    }

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = { currentViewType = ViewType.LIST }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_list_24),
                            contentDescription = "List View"
                        )
                    }

                    IconButton(
                        modifier = Modifier.width(iconWidth),
                        onClick = { currentViewType = ViewType.GRID }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_grid_view_24),
                            contentDescription = "Grid View"
                        )
                    }

                }
            }

            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))

                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


           when (currentViewType) {
                ViewType.GRID -> GridViewNotes(
                    navController = navController,
                    viewModel = viewModel,
                    scope = scope,
                    snackbarHostState = snackbarHostState,
                    notesList = state.notes
                )
                ViewType.LIST -> ListViewNotes(
                    navController = navController,
                    viewModel = viewModel,
                    scope = scope,
                    snackbarHostState = snackbarHostState,
                    notesList = state.notes
                )
            }
        }


        }
    }



