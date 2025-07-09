package com.example.proyectoappcliente.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoappcliente.data.model.Resena
import com.example.proyectoappcliente.databinding.ReviewItemBinding

class ResenaAdapter(
    private var reviews: List<Resena>
) : RecyclerView.Adapter<ResenaAdapter.ResenaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResenaViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResenaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResenaViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    fun updateData(newReviews: List<Resena>) {
        this.reviews = newReviews
        notifyDataSetChanged()
    }

    inner class ResenaViewHolder(private val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resena: Resena) {
            binding.txtReviewAuthor.text = resena.author
            binding.txtReviewComment.text = resena.comment
        }
    }
}

