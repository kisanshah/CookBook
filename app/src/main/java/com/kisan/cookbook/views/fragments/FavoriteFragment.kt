package com.kisan.cookbook.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kisan.cookbook.adapter.RecipeAdapter
import com.kisan.cookbook.databinding.FragmentFavoriteBinding
import com.kisan.cookbook.model.Recipe


class FavoriteFragment : Fragment(), RecipeAdapter.RecipeInterface {
    private lateinit var binding: FragmentFavoriteBinding
    lateinit var adapter: RecipeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val query: Query = FirebaseDatabase.getInstance().reference.child("Users")
            .child(uid)
            .child("Favorite")
        val options: FirebaseRecyclerOptions<Recipe> = FirebaseRecyclerOptions.Builder<Recipe>()
            .setQuery(query, Recipe::class.java)
            .build()
        adapter = RecipeAdapter(options, this)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

        }

        return binding.root
    }

    override fun onStart() {
        adapter.startListening()
        super.onStart()
    }

    override fun onStop() {
        adapter.stopListening()
        super.onStop()
    }

    override fun onClick(model: Recipe, ref: String) {
        val direction =
            FavoriteFragmentDirections.actionFavoriteToRecipeDetailFragment(
                reciepe = model, ref = ref
            )
        findNavController().navigate(direction)
    }
}