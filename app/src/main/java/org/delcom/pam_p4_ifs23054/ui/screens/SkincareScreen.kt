package org.delcom.pam_p4_ifs23054.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23054.R
import org.delcom.pam_p4_ifs23054.helper.ConstHelper
import org.delcom.pam_p4_ifs23054.helper.RouteHelper
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper
import org.delcom.pam_p4_ifs23054.network.skincares.data.ResponseSkincareData
import org.delcom.pam_p4_ifs23054.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23054.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincareViewModel
import org.delcom.pam_p4_ifs23054.ui.viewmodels.SkincaresUIState

@Composable
fun SkincareScreen(
    navController: NavHostController,
    skincareViewModel: SkincareViewModel
) {
    val uiStateSkincare by skincareViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var skincares by remember { mutableStateOf<List<ResponseSkincareData>>(emptyList()) }

    fun fetchData() {
        isLoading = true
        skincareViewModel.getAllSkincares(searchQuery.text)
    }

    LaunchedEffect(Unit) { fetchData() }

    LaunchedEffect(uiStateSkincare.skincares) {
        if (uiStateSkincare.skincares !is SkincaresUIState.Loading) {
            isLoading = false
            skincares = if (uiStateSkincare.skincares is SkincaresUIState.Success) {
                (uiStateSkincare.skincares as SkincaresUIState.Success).data
            } else emptyList()
        }
    }

    if (isLoading) { LoadingUI(); return }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Skincare",
            showBackButton = false,
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchAction = { fetchData() }
        )

        Box(modifier = Modifier.weight(1f)) {
            SkincareListUI(
                skincares = skincares,
                onOpen = { id ->
                    RouteHelper.to(navController, "skincares/$id")
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = {
                        RouteHelper.to(navController, ConstHelper.RouteNames.SkincareAdd.path)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Skincare")
                }
            }
        }

        BottomNavComponent(navController = navController)
    }
}

@Composable
fun SkincareListUI(
    skincares: List<ResponseSkincareData>,
    onOpen: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(skincares) { skincare ->
            SkincareItemUI(skincare, onOpen)
        }
    }

    if (skincares.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Tidak ada data!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun SkincareItemUI(
    skincare: ResponseSkincareData,
    onOpen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onOpen(skincare.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = ToolsHelper.getSkincareImageUrl(skincare.id),
                contentDescription = skincare.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = skincare.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = skincare.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
