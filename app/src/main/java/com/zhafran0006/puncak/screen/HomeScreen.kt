package com.zhafran0006.puncak.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhafran0006.puncak.R
import com.zhafran0006.puncak.data.MountainEntity
import com.zhafran0006.puncak.viewmodel.PeakViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PeakViewModel,
    onNavigate: (String) -> Unit,
    onAboutNavigate: () -> Unit
) {
    val mountains by viewModel.allMountains.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isGridLayout by viewModel.isGridLayout.collectAsState()
    var showThemeMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_home)) },
                actions = {
                    // Toggle Layout (List/Grid)
                    IconButton(onClick = { viewModel.toggleLayout(!isGridLayout) }) {
                        Icon(
                            imageVector = if (isGridLayout) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = if (isGridLayout) stringResource(R.string.layout_list) else stringResource(R.string.layout_grid)
                        )
                    }

                    // Dark Mode Toggle
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleTheme(it) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Theme Color Menu
                    Box {
                        IconButton(onClick = { showThemeMenu = true }) {
                            Icon(Icons.Default.Palette, contentDescription = stringResource(R.string.change_theme))
                        }
                        DropdownMenu(
                            expanded = showThemeMenu,
                            onDismissRequest = { showThemeMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Purple (Default)") },
                                onClick = { viewModel.setThemeColor(0); showThemeMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Blue") },
                                onClick = { viewModel.setThemeColor(1); showThemeMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Green") },
                                onClick = { viewModel.setThemeColor(2); showThemeMenu = false }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.about)) },
                                onClick = { onAboutNavigate(); showThemeMenu = false },
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (isGridLayout) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(mountains) { mtn ->
                    MountainCard(mtn, onNavigate)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(mountains) { mtn ->
                    MountainCard(mtn, onNavigate)
                }
            }
        }
    }
}

@Composable
fun MountainCard(mtn: MountainEntity, onNavigate: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onNavigate(mtn.name) }
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ciremai_hero),
                contentDescription = "Hero Image",
                modifier = Modifier.height(140.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(text = mtn.name, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.titleLarge)
            Text(text = mtn.location, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        }
    }
}
