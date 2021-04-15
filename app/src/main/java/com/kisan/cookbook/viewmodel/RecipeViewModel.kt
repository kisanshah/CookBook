@file:Suppress("unused")

package com.kisan.cookbook.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kisan.cookbook.model.Recipe
import com.kisan.cookbook.repository.RecipeRepository
import com.kisan.cookbook.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {

    val recipeUploadResult: LiveData<Resource<Boolean>> = repository.uploadRecipeResult
    val imageUploadResult: MutableLiveData<Resource<String>> = repository.uploadImageResult

    fun uploadRecipe(data: Recipe) {
        repository.uploadRecipe(data)
    }

    fun uploadImage(uri: Uri) {
        repository.uploadReciepeImage(uri)
    }
}