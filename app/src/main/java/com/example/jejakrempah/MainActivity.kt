package com.example.jejakrempah

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.jejakrempah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavController using NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up Toolbar as ActionBar (tanpa tombol back)
        setSupportActionBar(binding.toolbar)

        // Link BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Optional: Handle bottom navigation item selection
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.beranda -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nuspice -> {
                    navController.navigate(R.id.nuSpiceFragment)
                    true
                }
                R.id.camera -> {
                    navController.navigate(R.id.cameraFragment)
                    true
                }
                R.id.favorite -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        return true
    }
}
