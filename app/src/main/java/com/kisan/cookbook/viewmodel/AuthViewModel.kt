package com.kisan.cookbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.kisan.cookbook.repository.AuthRepository
import com.kisan.cookbook.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    var signInAnonymouslyResult: LiveData<Resource<AuthResult>> = authRepository.resultLiveData
    var userStateLiveData = authRepository.userLiveData

    fun signInAnonymously() {
        authRepository.signInAnonymously()
    }
}