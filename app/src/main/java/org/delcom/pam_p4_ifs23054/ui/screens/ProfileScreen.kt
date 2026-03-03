package org.delcom.pam_p4_ifs23054.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23054.R
import org.delcom.pam_p4_ifs23054.helper.ToolsHelper
import org.delcom.pam_p4_ifs23054.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23054.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23054.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23054.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23054.ui.viewmodels.PlantViewModel
import org.delcom.pam_p4_ifs23054.ui.viewmodels.ProfileUIState

@Composable
fun ProfileScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel
) {
    val uiStatePlant by plantViewModel.uiState.collectAsState()

    var isLoading by remember { mutableStateOf(true) }
    var profile by remember { mutableStateOf<ResponseProfile?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Muat data profil sekali saja
    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        plantViewModel.getProfile()
    }

    // Reaksi terhadap perubahan state profil
    LaunchedEffect(uiStatePlant.profile) {
        when (val state = uiStatePlant.profile) {
            is ProfileUIState.Success -> {
                profile = state.data
                isLoading = false
                errorMessage = null
            }
            is ProfileUIState.Error -> {
                isLoading = false
                // Tampilkan error, JANGAN langsung back() — itu yang menyebabkan infinite loop
                errorMessage = state.message
            }
            is ProfileUIState.Loading -> {
                // Tetap menunggu, tidak melakukan apa-apa
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Profile", showBackButton = false)

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> LoadingUI()

                errorMessage != null -> {
                    // Tampilkan pesan error dengan tombol retry
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "⚠️ Gagal memuat profil",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            isLoading = true
                            errorMessage = null
                            plantViewModel.getProfile()
                        }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                profile != null -> {
                    ProfileUI(profile = profile!!)
                }
            }
        }

        BottomNavComponent(navController = navController)
    }
}

@Composable
fun ProfileUI(profile: ResponseProfile) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header Profile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = ToolsHelper.getProfilePhotoUrl(),
                    contentDescription = "Photo Profil",
                    placeholder = painterResource(R.drawable.img_placeholder),
                    error = painterResource(R.drawable.img_placeholder),
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = profile.nama, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Text(
                    text = profile.username,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bio Section
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Tentang Saya",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(profile.tentang, fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewProfileUI() {
    DelcomTheme {
        ProfileUI(
            profile = ResponseProfile(
                nama = "Sri Diva Siagian",
                username = "ifs23054",
                tentang = "Mahasiswa Informatika"
            )
        )
    }
}