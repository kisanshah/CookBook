package com.kisan.cookbook.views.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.firebase.storage.FirebaseStorage
import com.kisan.cookbook.GlideApp
import com.kisan.cookbook.R
import com.kisan.cookbook.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private val safeArgs: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        binding.apply {
            val recipe = safeArgs.reciepe
            val ref = FirebaseStorage.getInstance().reference.child(recipe.imgUrl)
            GlideApp.with(requireContext())
                .load(ref)
                .centerCrop()
                .into(recipeImg)
            cuisineName.text = "Cuisine : ${recipe.cuisine}"
            recipeTitle.text = recipe.title
            recipeDesc.text = recipe.description
            recipeAuthor.text = "Author : ${recipe.author}"
            date.text = recipe.date
            makeTextBold(recipe.directions)
            recipe.ingredients.split(",").toList().forEach {
                addChip(it.trim())
            }
        }
        return binding.root
    }

    private fun addChip(s: String) {
        val chip = Chip(context)
        chip.text = s
        chip.isCheckable = true
        binding.chipGroup.addView(chip)
    }

    private fun makeTextBold(s: String) {
        val builder = SpannableStringBuilder(s)
        val str = "(Step)[ ][0-9]*".toRegex()
        val listOfSteps = str.findAll(s, 0).toList()
        Log.d("DEBUG", listOfSteps.toString())
        for (i in listOfSteps) {
            val styleSpan = StyleSpan(Typeface.BOLD)
            val colorSpan =
                ForegroundColorSpan(requireContext().resources.getColor(R.color.primary))
            builder.setSpan(
                styleSpan,
                i.range.first,
                i.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                colorSpan,
                i.range.first,
                i.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.recipeDirections.text = builder
    }
}