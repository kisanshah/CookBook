package com.kisan.cookbook.views.activity

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kisan.cookbook.R
import com.kisan.cookbook.databinding.ActivityAddRecipeBinding
import com.kisan.cookbook.model.Recipe
import com.kisan.cookbook.utils.Status
import com.kisan.cookbook.utils.Utils.Companion.getCurrentDate
import com.kisan.cookbook.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecipeBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        attachObserver()
        setUpViews()
        val cuisines = resources.getStringArray(R.array.cuisine_list)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, cuisines)
        binding.autoCompleteCuisine.setAdapter(arrayAdapter)

    }


    private fun setUpViews() {
        binding.apply {
            recipeIngredientLayout.setEndIconOnClickListener {
                addChip(binding.recipeIngredients.text)
            }

            recipeDirection.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    makeTextBold(binding.recipeDirection.editableText)
                }
            }

            pickImage.setOnClickListener {
                launchImagePickActivity.launch("image/*")
            }

            uploadBtn.setOnClickListener {
                if (imgUri != null) {
                    recipeViewModel.uploadImage(imgUri!!)
                } else {
                    Toast.makeText(applicationContext, "Please pick an Image", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun attachObserver() {
        recipeViewModel.recipeUploadResult.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    Snackbar.make(
                        binding.coordinatorLayout,
                        "Recipe Uploaded",
                        Snackbar.LENGTH_LONG
                    ).show()
                    finish()
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Snackbar.make(
                        binding.coordinatorLayout,
                        "Failed to Upload : ${it.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
        recipeViewModel.imageUploadResult.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    uploadReciepe(it.data.toString())
                }
                Status.LOADING -> {
                    Snackbar.make(
                        binding.coordinatorLayout,
                        "Uploading Recipe",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                Status.ERROR -> {
                    Snackbar.make(
                        binding.coordinatorLayout,
                        "Failed to Upload : ${it.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun uploadReciepe(img: String) {
        val title: String = binding.recipeTitle.text.toString()
        val description: String = binding.recipeDesc.text.toString()
        val ingredients: String = getIngredients()
        val directions: String = binding.recipeDirection.text.toString()
        val date: String = getCurrentDate()
        val isVeg: Boolean = true
        val author: String = "Kisan Shah"
        val cuisine: String = binding.autoCompleteCuisine.text.toString()
        val imgUrl: String = img
        val privateNote: String = binding.recipePrivateNote.text.toString()
        val prepTime: Int = binding.prepTime.text.toString().toInt()
        val cookTime: Int = binding.cookTime.text.toString().toInt()

        val recipe = Recipe(
            title,
            description,
            ingredients,
            directions,
            date,
            isVeg,
            author,
            cuisine,
            imgUrl,
            privateNote,
            cookTime,
            prepTime

        )
        recipeViewModel.uploadRecipe(recipe)
    }

    private fun getIngredients(): String {
        return binding.chipGroup.children.toList().joinToString(",") {
            (it as Chip).text
        }
    }

    private var launchImagePickActivity =
        registerForActivityResult(GetContent()) {
            imgUri = it
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.thumbnail)
        }

    private fun makeTextBold(s: Editable?) {
        val builder = SpannableStringBuilder(s.toString())
        val str = "(Step)[ ][0-9]*".toRegex()
        val listOfSteps = str.findAll(s.toString(), 0).toList()
        for (i in listOfSteps) {
            val styleSpan = StyleSpan(Typeface.BOLD)

            builder.setSpan(
                styleSpan,
                i.range.first,
                i.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.recipeDirection.text = builder
        }
    }


    private fun addChip(s: Editable?) {
        val trimmed = s.toString().trim()
        if (trimmed.length > 1) {
            val chip = Chip(this@AddRecipeActivity)
            chip.text = trimmed
            chip.isCloseIconVisible = true
            chip.closeIconTint = ColorStateList.valueOf(getColor(R.color.primary))
            chip.setOnCloseIconClickListener {
                binding.chipGroup.removeView(chip)
            }
            binding.chipGroup.addView(chip)
            s?.clear()
        }
    }
}