package com.kisan.cookbook.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.kisan.cookbook.model.Recipe
import com.kisan.cookbook.utils.Resource
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorageRef: StorageReference
) {
    var uploadRecipeResult = MutableLiveData<Resource<Boolean>>()
    var uploadImageResult = MutableLiveData<Resource<String>>()

    fun uploadRecipe(data: Recipe) {
        uploadRecipeResult.postValue(Resource.loading(true))
        firebaseDatabase.reference.child("Recipe")
            .push().setValue(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    uploadRecipeResult.postValue(Resource.success(it.isSuccessful))
                } else {
                    uploadRecipeResult.postValue(
                        Resource.error(
                            it.exception?.localizedMessage!!,
                            null
                        )
                    )
                }
            }
    }

    fun uploadReciepeImage(uri: Uri) {
        uploadImageResult.postValue(Resource.loading(null))
        val fileRef =
            firebaseStorageRef.child("images/${(0..1000).random() + System.currentTimeMillis()}")
        fileRef.putFile(uri).addOnCompleteListener {
            if (it.isSuccessful) {
                fileRef.downloadUrl.addOnCompleteListener { url ->
                    if (url.isSuccessful) {
                        uploadImageResult.postValue(Resource.success(url.result?.lastPathSegment))
                    } else {
                        uploadImageResult.postValue(
                            Resource.error(it.exception?.localizedMessage!!, null)
                        )
                    }
                }
            } else {
                uploadImageResult.postValue(Resource.error(it.exception?.localizedMessage!!, null))
            }
        }
    }
}