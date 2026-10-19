package com.example.superkahramanlarmanuel.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superkahramanlarmanuel.ui.theme.MarvelRed
import com.example.superkahramanlarmanuel.viewmodel.KahramanViewModel

@Composable
fun KahramanlarScreen(viewModel: KahramanViewModel, navController: NavController) {

    // ViewModel'daki state değişkenlerini collectAsState() ile gözlemliyoruz
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MarvelRed)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Arama kutusu (TextField)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) }, // ViewModel'daki fonksiyonu çağırıyoruz
            label = { Text("Karakter Ara...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // UI durumuna göre içeriği gösteriyoruz
        when (uiState) {
            is KahramanViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is KahramanViewModel.UiState.Success -> {
                val characters = (uiState as KahramanViewModel.UiState.Success).characters
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(characters) { kahraman ->
                        KahramanCard(
                            kahraman = kahraman,
                            onCardClick = {
                                navController.navigate("detay_ekrani/${kahraman.id}")
                            })
                    }
                }
            }
            is KahramanViewModel.UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = (uiState as KahramanViewModel.UiState.Error).message)
                }
            }
            is KahramanViewModel.UiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Sonuç bulunamadı.")
                }
            }
        }
    }
}