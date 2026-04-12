package com.zhafran0006.puncak.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhafran0006.puncak.R
import com.zhafran0006.puncak.viewmodel.PeakViewModel

// Data class Gear (kalau lu udah pisah file, ini dihapus aja biar nggak bentrok)
data class Gear(val name: String, var isPacked: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mountainName: String,
    viewModel: PeakViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // --- REAL-TIME STATE DARI VIEWMODEL ---
    val allGearMap by viewModel.allGearLists.collectAsState()
    val gearList = allGearMap[mountainName] ?: emptyList()

    // --- LOCAL STATE BUAT INPUT ---
    var inputText by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mountainName) },
                // --- INI DIA TOMBOL NAVIGASI BACK-NYA ---
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Aspek Kebaruan: Dropdown Tipe Pendakian
            var expanded by remember { mutableStateOf(false) }
            val options = listOf("Tektok", "Camping")
            var selectedOption by remember { mutableStateOf(options[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    label = { Text("Tipe Pendakian") }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOption = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Aspek Fungsionalitas: Input Barang
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it; isError = false },
                label = { Text(stringResource(R.string.input_hint)) },
                isError = isError,
                supportingText = { if (isError) Text(stringResource(R.string.error_empty)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.addGear(mountainName, inputText)
                        inputText = ""
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.btn_add_gear))
            }

            // Aspek Fungsionalitas: Implicit Intent Google Maps
            Button(
                onClick = {
                    val uri = Uri.parse("geo:0,0?q=$mountainName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(stringResource(R.string.btn_maps))
            }

            // Aspek Kebaruan: List Checklist
            LazyColumn {
                items(gearList) { gear ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = gear.isPacked,
                            onCheckedChange = {
                                viewModel.toggleGear(mountainName, gear)
                            }
                        )
                        Text(gear.name)
                    }
                }
            }
        }
    }
}