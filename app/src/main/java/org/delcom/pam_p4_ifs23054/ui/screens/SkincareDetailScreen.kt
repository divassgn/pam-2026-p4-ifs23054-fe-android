package org.delcom.pam_p4_ifs23054.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23054.R
import org.delcom.pam_p4_ifs23054.helper.ConstHelper
import org.delcom.pam_p4_ifs23054.helper.RouteHelper
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper
import org.delcom.pam_p4_ifs23054.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareData
import org.delcom.pam_p4_ifs23054.ui.components.BottomDialog
import org.delcom.pam_p4_ifs23054.ui.components.BottomDialogType
import org.delcom.pam_p4_ifs23054.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23054.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarMenuItem
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareActionUIState
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareUIState
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareViewModel

@Composable
fun SkincareDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    skincareViewModel: SkincareViewModel,
    skincareId: String
) {
    val uiStateSkincare by skincareViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
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
            }
        }
    }

    LaunchedEffect(uiStateSkincare.skincareAction) {
        when (val state = uiStateSkincare.skincareAction) {
            is SkincareActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost, SnackBarType.SUCCESS, state.message)
                RouteHelper.to(navController, ConstHelper.RouteNames.Skincares.path, true)
                uiStateSkincare.skincare = SkincareUIState.Loading
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

    val menuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            route = null,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.SkincareEdit.path.replace("{skincareId}", skincare!!.id)
                )
            }
        ),
        TopAppBarMenuItem(
            text = "Hapus Data",
            icon = Icons.Filled.Delete,
            route = null,
            onClick = { isConfirmDelete = true }
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = skincare!!.nama,
            showBackButton = true,
            customMenuItems = menuItems
        )
        Box(modifier = Modifier.weight(1f)) {
            SkincareDetailUI(skincare = skincare!!)
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Konfirmasi Hapus Data",
                message = "Apakah Anda yakin ingin menghapus data ini?",
                confirmText = "Ya, Hapus",
                onConfirm = {
                    isLoading = true
                    skincareViewModel.deleteSkincare(skincareId)
                },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun SkincareDetailUI(skincare: ResponseSkincareData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Gambar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            AsyncImage(
                model = ToolsHelper.getSkincareImageUrl(skincare.id),
                contentDescription = skincare.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = skincare.nama,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Deskripsi
        InfoCard(title = "Deskripsi", content = skincare.deskripsi)

        // Manfaat
        InfoCard(title = "Manfaat", content = skincare.manfaat)

        // Efek Samping
        InfoCard(title = "Efek Samping", content = skincare.efekSamping)
    }
}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
            Text(text = content, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
