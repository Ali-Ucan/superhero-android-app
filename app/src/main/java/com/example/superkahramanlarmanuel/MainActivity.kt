package com.example.superkahramanlarmanuel

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.superkahramanlarmanuel.repository.MarvelRepository
import com.example.superkahramanlarmanuel.screens.KahramanDetayScreen
import com.example.superkahramanlarmanuel.screens.KahramanlarScreen
import com.example.superkahramanlarmanuel.service.KahramanlarAPI
import com.example.superkahramanlarmanuel.ui.theme.SuperKahramanlarManuelTheme
import com.example.superkahramanlarmanuel.viewmodel.KahramanViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.superkahramanlarmanuel.model.FavoritesManager

object RetrofitClient {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    val api: KahramanlarAPI by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(KahramanlarAPI::class.java)
    }
}

object AppRepository {
    lateinit var repository: MarvelRepository
    private lateinit var favoritesManager: FavoritesManager

    fun initialize(context: Context, api: KahramanlarAPI) {
        favoritesManager = FavoritesManager(context)
        repository = MarvelRepository(api, favoritesManager)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppRepository.initialize(applicationContext, RetrofitClient.api)

        setContent {
            SuperKahramanlarManuelTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "kahramanlar_listesi"
    ) {
        composable("kahramanlar_listesi") {
            val viewModel: KahramanViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(KahramanViewModel::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            return KahramanViewModel(AppRepository.repository) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            )
            KahramanlarScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            "detay_ekrani/{kahramanId}",
            arguments = listOf(navArgument("kahramanId") { type = NavType.IntType })
        ) { backStackEntry ->
            val kahramanId = backStackEntry.arguments?.getInt("kahramanId")
            val viewModel: KahramanViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(KahramanViewModel::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            return KahramanViewModel(AppRepository.repository) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            )

            if (kahramanId != null) {
                KahramanDetayScreen(
                    kahramanId = kahramanId,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}