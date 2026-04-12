package com.zhafran0006.puncak.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhafran0006.puncak.R
import com.zhafran0006.puncak.model.DataProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.title_home)) }) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(DataProvider.mountains) { mtn ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onNavigate(mtn.name) }
                ) {
                    Column {
                        // Menampilkan gambar raster sesuai rubrik
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
        }
    }
}