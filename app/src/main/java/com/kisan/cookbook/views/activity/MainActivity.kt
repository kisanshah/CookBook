package com.kisan.cookbook.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kisan.cookbook.R
import com.kisan.cookbook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragment)
        binding.apply {
            bottomNavView.setupWithNavController(navController)
            addRecipeFab.setOnClickListener {
                goToAddRecipe()
            }
        }
    }

    private fun goToAddRecipe() {
        startActivity(Intent(this, AddRecipeActivity::class.java))
    }

}