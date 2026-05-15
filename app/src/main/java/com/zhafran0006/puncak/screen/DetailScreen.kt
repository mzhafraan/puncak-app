package com.zhafran0006.puncak.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhafran0006.puncak.R
import com.zhafran0006.puncak.data.GearEntity
import com.zhafran0006.puncak.viewmodel.PeakViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mountainName: String,
    viewModel: PeakViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val gearList by viewModel.getGearsForMountain(mountainName).collectAsState(initial = emptyList())

    var inputText by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }

    var gearToUpdate by remember { mutableStateOf<GearEntity?>(null) }
    var updateText by remember { mutableStateOf("") }
    var gearToDelete by remember { mutableStateOf<GearEntity?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(mountainName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.cancel))
                    }
                },
                actions = {
                    // Share feature
                    IconButton(onClick = {
                        val gearText = gearList.joinToString("\n") { "- ${it.gearName} ${if (it.isPacked) "✅" else "❌"}" }
                        val shareContent = context.getString(R.string.share_title, mountainName) + ":\n" + gearText
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareContent)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { 
                    inputText = it
                    if (it.isNotBlank()) isError = false 
                },
                label = { Text(stringResource(R.string.add_gear_hint)) },
                isError = isError,
                supportingText = { if (isError) Text(stringResource(R.string.error_empty)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.addGear(mountainName, inputText)
                        inputText = ""
                        isError = false
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.btn_add_gear))
            }

            // Implicit Intent Google Maps
            Button(
                onClick = {
                    val uri = Uri.parse("geo:0,0?q=$mountainName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(stringResource(R.string.btn_maps))
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (gearToUpdate != null) {
                AlertDialog(
                    onDismissRequest = { gearToUpdate = null },
                    title = { Text(stringResource(R.string.update_item)) },
                    text = {
                        OutlinedTextField(
                            value = updateText,
                            onValueChange = { updateText = it },
                            label = { Text(stringResource(R.string.input_hint)) }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            gearToUpdate?.let { viewModel.updateGearName(it, updateText) }
                            gearToUpdate = null
                        }) { Text(stringResource(R.string.update_item)) }
                    },
                    dismissButton = {
                        TextButton(onClick = { gearToUpdate = null }) { Text(stringResource(R.string.cancel)) }
                    }
                )
            }

            if (gearToDelete != null) {
                AlertDialog(
                    onDismissRequest = { gearToDelete = null },
                    title = { Text(stringResource(R.string.delete_item)) },
                    text = { Text(stringResource(R.string.delete_confirm, gearToDelete?.gearName ?: "")) },
                    confirmButton = {
                        TextButton(onClick = {
                            gearToDelete?.let { gear ->
                                viewModel.moveToRecycleBin(gear)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.item_deleted),
                                        actionLabel = context.getString(R.string.undo),
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.toggleGear(gear.copy(isDeleted = false))
                                    }
                                }
                            }
                            gearToDelete = null
                        }) { Text(stringResource(R.string.confirm_delete)) }
                    },
                    dismissButton = {
                        TextButton(onClick = { gearToDelete = null }) { Text(stringResource(R.string.cancel)) }
                    }
                )
            }

            Text(text = stringResource(R.string.list_title), style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(gearList) { gear ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = gear.isPacked,
                            onCheckedChange = { viewModel.toggleGear(gear) }
                        )
                        Text(text = gear.gearName, modifier = Modifier.weight(1f))
                        
                        IconButton(onClick = { 
                            gearToUpdate = gear
                            updateText = gear.gearName 
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.update_item))
                        }

                        IconButton(onClick = { gearToDelete = gear }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_item), tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
