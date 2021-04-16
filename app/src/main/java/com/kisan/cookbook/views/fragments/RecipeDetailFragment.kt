package com.kisan.cookbook.views.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage
import com.kisan.cookbook.GlideApp
import com.kisan.cookbook.R
import com.kisan.cookbook.adapter.CommentAdapter
import com.kisan.cookbook.databinding.FragmentRecipeDetailBinding
import com.kisan.cookbook.model.Comment
import com.kisan.cookbook.utils.Utils

class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private val safeArgs: RecipeDetailFragmentArgs by navArgs()
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)


        val recipe = safeArgs.reciepe
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = safeArgs.ref
        setUpView()
        binding.favBtn.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(uid)
                .child("Favorite")
                .push()
                .setValue(recipe).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to Add", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        val query: Query = FirebaseDatabase.getInstance().reference.child("Recipe").child(ref)
            .child("Comments")
        val option = FirebaseRecyclerOptions.Builder<Comment>()
            .setQuery(query, Comment::class.java)
            .build()
        commentAdapter = CommentAdapter(option)
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.commentRecyclerView.adapter = commentAdapter


        binding.commentLayout.setEndIconOnClickListener {
            val commentInput = binding.commentInput.text.toString()
            if (commentInput.isNotBlank()) {
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val comment = Comment(userId, commentInput, Utils.getCurrentDate(), "Guest")
                FirebaseDatabase.getInstance().reference.child("Recipe").child(ref)
                    .child("Comments").push()
                    .setValue(comment).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Comment Added", Toast.LENGTH_SHORT).show()
                            binding.commentInput.setText("")
                        } else {
                            Toast.makeText(context, "Failed to Add Comment", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUpView() {
        val recipe = safeArgs.reciepe

        binding.apply {
            val ref = FirebaseStorage.getInstance().reference.child(recipe.imgUrl)

            privateNoteLayout.setEndIconOnClickListener {
                if (recipe.privateNote != privateNoteInput.text.toString()) {
                    FirebaseDatabase.getInstance().reference.child("Recipe").child(safeArgs.ref)
                        .child("privateNote").setValue(privateNoteInput.text.toString())
                }
            }


            if (recipe.authorId == FirebaseAuth.getInstance().currentUser!!.uid) {
                privateNoteLayout.visibility = VISIBLE
                privateNoteInput.setText(recipe.privateNote)
            }
            val s =
                "• Prep : ${recipe.cookTime}mins\n• Cook : ${recipe.cookTime}mins\n• Total : ${recipe.cookTime + recipe.prepTime}mins"
            timeRequired.text = s
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
    }

    override fun onStart() {
        commentAdapter.startListening()
        super.onStart()
    }

    override fun onStop() {
        commentAdapter.stopListening()
        super.onStop()
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
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary))
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