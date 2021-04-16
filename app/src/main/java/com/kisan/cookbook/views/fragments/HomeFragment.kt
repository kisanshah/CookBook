package com.kisan.cookbook.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kisan.cookbook.adapter.RecipeAdapter
import com.kisan.cookbook.databinding.FragmentHomeBinding
import com.kisan.cookbook.model.Recipe


class HomeFragment : Fragment(), RecipeAdapter.RecipeInterface {

    private lateinit var binding: FragmentHomeBinding
    lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val query: Query = FirebaseDatabase.getInstance().reference.child("Recipe")
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
            HomeFragmentDirections.actionHomeToRecipeDetailFragment(reciepe = model, ref = ref)
        findNavController().navigate(direction)
    }
}