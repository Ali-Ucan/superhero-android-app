package com.example.superkahramanlarmanuel.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.superkahramanlarmanuel.model.Kahramanlar
import com.example.superkahramanlarmanuel.ui.theme.MarvelRed
import com.example.superkahramanlarmanuel.viewmodel.KahramanViewModel

@Composable
fun KahramanDetayScreen(
    kahramanId: Int,
    navController: NavController,
    viewModel: KahramanViewModel
) {
    val selectedCharacter by viewModel.selectedCharacter.collectAsState()

    LaunchedEffect(key1 = kahramanId) {
        viewModel.getCharacterById(kahramanId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MarvelRed)
    ) {
        selectedCharacter?.let { kahraman ->
            // Bütün içeriği bu bloğun içine koy
            DetayEkrani(kahraman = kahraman)

            IconButton(
                onClick = { viewModel.toggleFavorite(kahraman) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = if (kahraman.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Favori",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
}}

@Composable
fun DetayEkrani(kahraman: Kahramanlar) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = kahraman.getImageUrl(),
            contentDescription = kahraman.name,
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = kahraman.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (kahraman.description.isNotEmpty()) {
            Text(
                text = kahraman.description,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Justify
            )
        } else {
            Text(
                text = "Açıklama bulunmuyor.",
                fontSize = 16.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
        }
    }
}