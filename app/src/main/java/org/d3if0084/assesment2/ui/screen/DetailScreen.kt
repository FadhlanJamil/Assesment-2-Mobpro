package org.d3if0084.assesment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0084.assesment2.R
import org.d3if0084.assesment2.database.PesanDb
import org.d3if0084.assesment2.ui.theme.Assesment2Theme
import org.d3if0084.assesment2.util.ViewModelFactory

const val KEY_ID_PESAN = "idPesan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {

    val context = LocalContext.current
    val db = PesanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember {
        mutableStateOf("")
    }
    var pesanan by remember {
        mutableStateOf("")
    }
    val radioOptions = listOf(
        "Ditempat",
        "Dibungkus",
    )
    var makandi by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getPesan(id) ?: return@LaunchedEffect
        nama = data.nama
        makandi = data.makandi
        pesanan = data.pesanan
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_pesanan))
                    else
                        Text(text = stringResource(id = R.string.edit_Pesanan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if(nama == "" || makandi == "" || pesanan == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(nama, makandi, pesanan)
                        } else {
                            viewModel.update(id, nama, makandi, pesanan)
                        }

                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction {showDialog = true}
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    )
    { padding ->
        FormPesanan(
            nama = nama,
            onNamaChange = {nama = it},
            pesanan = pesanan,
            onPesananChange = { pesanan = it },
            makandi = makandi,
            onMakandiChange = { makandi = it },
            listmakandi = radioOptions,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.hapus))},
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormPesanan (
    nama: String, onNamaChange: (String) -> Unit,
    makandi: String, onMakandiChange: (String) -> Unit,
    pesanan: String, onPesananChange: (String) -> Unit,
    listmakandi: List<String>,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nama_lengkap)
        )
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(id = R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(id = R.string.order_by),
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .fillMaxWidth()
        ) {
            listmakandi.forEach { text ->
                MakanDi(
                    label = text,
                    isSelected = makandi == text,
                    modifier = Modifier
                        .selectable(
                            selected = makandi == text,
                            onClick = { onMakandiChange(text) },
                            role = Role.RadioButton
                        )
                        .padding(16.dp)
                )
            }
        }
        OutlinedTextField(
            value = pesanan,
            onValueChange = { onPesananChange(it) },
            label = { Text(text = stringResource(id = R.string.pesanan)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MakanDi(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview(){
    Assesment2Theme {
        DetailScreen(rememberNavController())
    }
}