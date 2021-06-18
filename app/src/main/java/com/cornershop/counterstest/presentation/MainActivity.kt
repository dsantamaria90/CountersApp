package com.cornershop.counterstest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cornershop.counterstest.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavHost {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(
            navController,
            AppBarConfiguration(setOf(R.id.welcome_fragment))
        )
    }

    override fun getNavController() =
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
            .navController

    override fun onSupportNavigateUp() = navController.navigateUp() ||
        super.onSupportNavigateUp()
}