package com.kisan.cookbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.kisan.cookbook.GlideApp
import com.kisan.cookbook.databinding.RecipeItemBinding
import com.kisan.cookbook.model.Recipe

class RecipeAdapter(
    options: FirebaseRecyclerOptions<Recipe>,
    val recipeInterface: RecipeInterface
) :
    FirebaseRecyclerAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(
        options
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }


    inner class RecipeViewHolder(var viewBinding: RecipeItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int, model: Recipe) {
        holder.viewBinding.apply {
            cardView.setOnClickListener { recipeInterface.onClick(model) }
            val ref = FirebaseStorage.getInstance().reference.child(model.imgUrl)
            GlideApp.with(this.root).load(ref).centerCrop().into(imageView)
            date.text = model.date
            title.text = model.title
            content.text = model.description
            user.text = model.author
        }
    }

    interface RecipeInterface {
        fun onClick(model: Recipe)
    }

}