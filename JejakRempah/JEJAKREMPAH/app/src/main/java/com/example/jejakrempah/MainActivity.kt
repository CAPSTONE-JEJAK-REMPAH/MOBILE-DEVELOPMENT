package com.example.jejakrempah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.jejakrempah.Fragment.FavoriteFragment
import com.example.jejakrempah.Fragment.HomeFragment
import com.example.jejakrempah.Fragment.NuSpiceFragment
import com.example.jejakrempah.Fragment.SettingFragment
import com.example.jejakrempah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.home -> replaceFragment(HomeFragment())
                R.id.nuspice -> replaceFragment(NuSpiceFragment())
                R.id.favorite -> replaceFragment(FavoriteFragment())
                R.id.settings -> replaceFragment(SettingFragment())

                else ->{



                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }


}