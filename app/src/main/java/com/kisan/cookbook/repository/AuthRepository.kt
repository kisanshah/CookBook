package com.kisan.cookbook.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kisan.cookbook.utils.Resource
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    var resultLiveData: MutableLiveData<Resource<AuthResult>> =
        MutableLiveData<Resource<AuthResult>>()

    var userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData<FirebaseUser>()

    init {
        userLiveData.postValue(auth.currentUser)
    }

    fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener {
            if (it.isSuccessful) {
                resultLiveData.postValue(Resource.success(it.result))
                userLiveData.postValue(auth.currentUser)
            } else {
                resultLiveData.postValue(Resource.error(it.exception.toString(), null))
            }
        }
    }


}