package com.redcoding.sousers.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.redcoding.sousers.ui.navigation.UserDetailsDestination
import com.redcoding.sousers.ui.navigation.UserListDestination
import com.redcoding.sousers.ui.userdetails.UserDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class UserListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = UserListDestination) {
                composable<UserListDestination> {
                    UserListScreen(
                        viewModel = hiltViewModel(),
                        onUserClick = { userId -> navController.navigate(UserDetailsDestination(userId)) },
                    )
                }
                composable<UserDetailsDestination> {
                    UserDetailsScreen(viewModel = hiltViewModel())
                }
            }
        }
    }
}
