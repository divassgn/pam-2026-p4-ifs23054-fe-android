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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23054.R
import org.delcom.pam_p4_ifs23054.helper.AlertHelper
import org.delcom.pam_p4_ifs23054.helper.AlertState
import org.delcom.pam_p4_ifs23054.helper.AlertType
import org.delcom.pam_p4_ifs23054.helper.ConstHelper
import org.delcom.pam_p4_ifs23054.helper.RouteHelper
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareData
import org.delcom.pam_p4_ifs23054.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23054.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareActionUIState
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareUIState
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareViewModel

@Composable
fun SkincareEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    skincareViewModel: SkincareViewModel,
    skincareId: String
) {
    val uiStateSkincare by skincareViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var skincare by remember { mutableStateOf<ResponseSkincareData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiStateSkincare.skincareAction = SkincareActionUIState.Loading
        uiStateSkincare.skincare = SkincareUIState.Loading
        skincareViewModel.getSkincareById(skincareId)
    }

    LaunchedEffect(uiStateSkincare.skincare) {
        if (uiStateSkincare.skincare !is SkincareUIState.Loading) {
            if (uiStateSkincare.skincare is SkincareUIState.Success) {
                skincare = (uiStateSkincare.skincare as SkincareUIState.Success).data
                isLoading = false
            } else {
                RouteHelper.back(navController)
                isLoading = false
            }
        }
    }

    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        manfaat: String,
        efekSamping: String,
        file: Uri? = null
    ) {
        isLoading = true
        var filePart: MultipartBody.Part? = null
        if (file != null) filePart = uriToMultipart(context, file, "file")

        skincareViewModel.putSkincare(
            skincareId = skincareId,
            nama = nama.toRequestBodyText(),
            deskripsi = deskripsi.toRequestBodyText(),
            manfaat = manfaat.toRequestBodyText(),
            efekSamping = efekSamping.toRequestBodyText(),
            file = filePart
        )
    }

    LaunchedEffect(uiStateSkincare.skincareAction) {
        when (val state = uiStateSkincare.skincareAction) {
            is SkincareActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.SUCCESS, state.message)
                RouteHelper.to(
                    navController = navController,
                    destination = ConstHelper.RouteNames.SkincareDetail.path.replace("{skincareId}", skincareId),
                    popUpTo = ConstHelper.RouteNames.SkincareDetail.path.replace("{skincareId}", skincareId),
                    removeBackStack = true
                )
                isLoading = false
            }
            is SkincareActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.ERROR, state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading || skincare == null) { LoadingUI(); return }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Ubah Skincare", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            SkincareEditUI(skincare = skincare!!, onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun SkincareEditUI(
    skincare: ResponseSkincareData,
    onSave: (Context, String, String, String, String, Uri?) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(skincare.nama) }
    var dataDeskripsi by remember { mutableStateOf(skincare.deskripsi) }
    var dataManfaat by remember { mutableStateOf(skincare.manfaat) }
    var dataEfekSamping by remember { mutableStateOf(skincare.efekSamping) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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
                AsyncImage(
                    model = dataFile ?: ToolsHelper.getSkincareImageUrl(skincare.id),
                    contentDescription = "Pratinjau Gambar",
                    placeholder = painterResource(R.drawable.img_placeholder),
                    error = painterResource(R.drawable.img_placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tap untuk mengganti gambar", style = MaterialTheme.typography.bodySmall)
        }

        val fieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        OutlinedTextField(value = dataNama, onValueChange = { dataNama = it }, colors = fieldColors,
            label = { Text("Nama", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        OutlinedTextField(value = dataDeskripsi, onValueChange = { dataDeskripsi = it }, colors = fieldColors,
            label = { Text("Deskripsi", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        OutlinedTextField(value = dataManfaat, onValueChange = { dataManfaat = it }, colors = fieldColors,
            label = { Text("Manfaat", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        OutlinedTextField(value = dataEfekSamping, onValueChange = { dataEfekSamping = it }, colors = fieldColors,
            label = { Text("Efek Samping", color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 5, minLines = 3
        )

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                if (dataNama.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Nama tidak boleh kosong!"); return@FloatingActionButton }
                if (dataDeskripsi.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Deskripsi tidak boleh kosong!"); return@FloatingActionButton }
                if (dataManfaat.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Informasi manfaat tidak boleh kosong!"); return@FloatingActionButton }
                if (dataEfekSamping.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Informasi efek samping tidak boleh kosong!"); return@FloatingActionButton }
                onSave(context, dataNama, dataDeskripsi, dataManfaat, dataEfekSamping, dataFile)
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
            confirmButton = { TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") } }
        )
    }
}
