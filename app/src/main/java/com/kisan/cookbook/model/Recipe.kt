package com.kisan.cookbook.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val title: String = "",
    val description: String = "",
    val ingredients: String = "",
    val directions: String = "",
    val date: String = "",
    val isVeg: Boolean = false,
    val author: String = "",
    val cuisine: String = "",
    val imgUrl: String = "",
    val privateNote: String = "",
    val cookTime: Int = 0,
    val prepTime: Int = 0
) : Parcelable
