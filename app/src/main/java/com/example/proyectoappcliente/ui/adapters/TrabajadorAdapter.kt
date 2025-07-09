package com.example.proyectoappcliente.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoappcliente.R
import com.example.proyectoappcliente.data.model.Trabajador
import com.example.proyectoappcliente.databinding.WorkerItemBinding

class TrabajadorAdapter(
    private var workers: List<Trabajador>,
    private val onItemClick: (Trabajador) -> Unit
) : RecyclerView.Adapter<TrabajadorAdapter.TrabajadorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrabajadorViewHolder {
        val binding = WorkerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrabajadorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrabajadorViewHolder, position: Int) {
        holder.bind(workers[position])
    }

    override fun getItemCount(): Int = workers.size

    fun updateData(newWorkers: List<Trabajador>) {
        this.workers = newWorkers
        notifyDataSetChanged()
    }

    inner class TrabajadorViewHolder(private val binding: WorkerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trabajador: Trabajador) {
            val fullName = "${trabajador.user.name} ${trabajador.user.lastName}"
            binding.txtWorkerName.text = fullName
            binding.txtWorkerRating.text = "${trabajador.averageRating}% - ${trabajador.reviewsCount} trabajos"

            Glide.with(itemView.context)
                .load(trabajador.pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into(binding.imgWorker)

            itemView.setOnClickListener { onItemClick(trabajador) }
        }
    }
}

