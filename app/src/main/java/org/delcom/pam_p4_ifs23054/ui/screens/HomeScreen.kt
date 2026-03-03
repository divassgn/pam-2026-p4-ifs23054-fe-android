package org.delcom.pam_p4_ifs23054.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23054.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23054.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23054.ui.theme.DelcomTheme

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Home", false)
        Box(modifier = Modifier.weight(1f)) {
            HomeUI()
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun HomeUI() {
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        // Judul Aplikasi
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = "\uD83E\uDDD4 Delcom Skincare \uD83E\uDDD4",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        // Subjudul
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Temukan produk skincare terbaik untuk kulit sehat dan bercahaya \u2728",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }

        // Baris ikon skincare
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(style = MaterialTheme.typography.headlineLarge, text = "\uD83D\uDC8E", modifier = Modifier.padding(16.dp))
            }

            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(style = MaterialTheme.typography.headlineLarge, text = "\uD83C\uDF38", modifier = Modifier.padding(16.dp))
            }

            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(style = MaterialTheme.typography.headlineLarge, text = "\uD83E\uDDD6", modifier = Modifier.padding(16.dp))
            }

            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(style = MaterialTheme.typography.headlineLarge, text = "\uD83C\uDF1F", modifier = Modifier.padding(16.dp))
            }
        }

        // Info cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "\uD83C\uDF3F Manfaat",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Pelajari manfaat dari setiap produk skincare",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "\u26A0\uFE0F Efek Samping",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Kenali efek samping sebelum pakai",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewHomeUI() {
    DelcomTheme {
        HomeUI()
    }
}