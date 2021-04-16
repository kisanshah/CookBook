package com.kisan.cookbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.kisan.cookbook.databinding.CommentItemBinding
import com.kisan.cookbook.model.Comment

class CommentAdapter(options: FirebaseRecyclerOptions<Comment>) :
    FirebaseRecyclerAdapter<Comment, CommentAdapter.CommentViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }


    inner class CommentViewHolder(var viewBinding: CommentItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, model: Comment) {
        holder.viewBinding.apply {
            date.text = model.date
            user.text = model.user
            comment.text = model.comment
        }
    }
}