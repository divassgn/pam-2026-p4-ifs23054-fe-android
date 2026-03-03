package org.delcom.pam_p4_ifs23054.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23054.R
import org.delcom.pam_p4_ifs23054.helper.AlertHelper
import org.delcom.pam_p4_ifs23054.helper.AlertState
import org.delcom.pam_p4_ifs23054.helper.AlertType
import org.delcom.pam_p4_ifs23054.helper.ConstHelper
import org.delcom.pam_p4_ifs23054.helper.RouteHelper
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23054.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23054.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareActionUIState
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareViewModel

@Composable
fun SkincareAddScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    skincareViewModel: SkincareViewModel
) {
    val uiStateSkincare by skincareViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        uiStateSkincare.skincareAction = SkincareActionUIState.Loading
    }

    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        manfaat: String,
        efekSamping: String,
        file: Uri
    ) {
        isLoading = true
        skincareViewModel.postSkincare(
            nama = nama.toRequestBodyText(),
            deskripsi = deskripsi.toRequestBodyText(),
            manfaat = manfaat.toRequestBodyText(),
            efekSamping = efekSamping.toRequestBodyText(),
            file = uriToMultipart(context, file, "file")
        )
    }

    LaunchedEffect(uiStateSkincare.skincareAction) {
        when (val state = uiStateSkincare.skincareAction) {
            is SkincareActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.SUCCESS, "Berhasil menambahkan skincare!")
                RouteHelper.to(navController, ConstHelper.RouteNames.Skincares.path, true)
                isLoading = false
            }
            is SkincareActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.ERROR, state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading) { LoadingUI(); return }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Tambah Skincare", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            SkincareAddUI(onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun SkincareAddUI(
    onSave: (Context, String, String, String, String, Uri) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf("") }
    var dataDeskripsi by remember { mutableStateOf("") }
    var dataManfaat by remember { mutableStateOf("") }
    var dataEfekSamping by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val deskripsiFocus = remember { FocusRequester() }
    val manfaatFocus = remember { FocusRequester() }
    val efekFocus = remember { FocusRequester() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> dataFile = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Gambar
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (dataFile != null) {
                    AsyncImage(
                        model = dataFile,
                        contentDescription = "Pratinjau Gambar",
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Pilih Gambar", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tap untuk mengganti gambar", style = MaterialTheme.typography.bodySmall)
        }

        // Nama
        OutlinedTextField(
            value = dataNama, onValueChange = { dataNama = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = { Text("Nama", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { deskripsiFocus.requestFocus() })
        )

        // Deskripsi
        OutlinedTextField(
            value = dataDeskripsi, onValueChange = { dataDeskripsi = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = { Text("Deskripsi", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp).focusRequester(deskripsiFocus),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { manfaatFocus.requestFocus() }),
            maxLines = 5, minLines = 3
        )

        // Manfaat
        OutlinedTextField(
            value = dataManfaat, onValueChange = { dataManfaat = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = { Text("Manfaat", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp).focusRequester(manfaatFocus),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { efekFocus.requestFocus() }),
            maxLines = 5, minLines = 3
        )

        // Efek Samping
        OutlinedTextField(
            value = dataEfekSamping, onValueChange = { dataEfekSamping = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = { Text("Efek Samping", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp).focusRequester(efekFocus),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                if (dataFile == null) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Gambar tidak boleh kosong!")
                    return@FloatingActionButton
                }
                if (dataNama.isEmpty()) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Nama tidak boleh kosong!")
                    return@FloatingActionButton
                }
                if (dataDeskripsi.isEmpty()) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Deskripsi tidak boleh kosong!")
                    return@FloatingActionButton
                }
                if (dataManfaat.isEmpty()) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Informasi manfaat tidak boleh kosong!")
                    return@FloatingActionButton
                }
                if (dataEfekSamping.isEmpty()) {
                    AlertHelper.show(alertState, AlertType.ERROR, "Informasi efek samping tidak boleh kosong!")
                    return@FloatingActionButton
                }
                onSave(context, dataNama, dataDeskripsi, dataManfaat, dataEfekSamping, dataFile!!)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Simpan Data")
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = { AlertHelper.dismiss(alertState) },
            title = { Text(alertState.value.type.title) },
            text = { Text(alertState.value.message) },
            confirmButton = {
                TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") }
            }
        )
    }
}
