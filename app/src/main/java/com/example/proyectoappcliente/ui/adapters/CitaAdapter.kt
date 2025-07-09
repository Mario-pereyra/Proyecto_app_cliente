package com.example.proyectoappcliente.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoappcliente.data.model.Cita
import com.example.proyectoappcliente.databinding.AppointmentItemBinding

class CitaAdapter(
    private var appointments: List<Cita>,
    private val onItemClick: (Cita) -> Unit
) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = AppointmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    override fun getItemCount(): Int = appointments.size

    fun updateData(newAppointments: List<Cita>) {
        this.appointments = newAppointments
        notifyDataSetChanged()
    }

    inner class CitaViewHolder(private val binding: AppointmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cita: Cita) {
            binding.txtWorkerName.text = cita.worker.user?.name ?: "Trabajador no asignado"
            binding.txtCategory.text = cita.category.name

            // Lógica de visualización condicional
            when (cita.status) {
                0 -> { // PENDIENTE
                    binding.txtAppointmentStatus.text = "Estado: Pendiente de concretar"
                    binding.txtAppointmentDateTime.visibility = View.GONE
                }
                1 -> { // SOLICITADA
                    binding.txtAppointmentStatus.text = "Estado: Solicitud enviada"
                    binding.txtAppointmentDateTime.visibility = View.VISIBLE
                    binding.txtAppointmentDateTime.text = "Fecha: ${cita.appointmentDate} - Hora: ${cita.appointmentTime}"
                }
                2 -> { // ACEPTADA
                    binding.txtAppointmentStatus.text = "Estado: Aceptada"
                    binding.txtAppointmentDateTime.visibility = View.VISIBLE
                    binding.txtAppointmentDateTime.text = "Fecha: ${cita.appointmentDate} - Hora: ${cita.appointmentTime}"
                }
                3 -> { // FINALIZADA
                    binding.txtAppointmentStatus.text = "Estado: Finalizada (Clic para calificar)"
                    binding.txtAppointmentDateTime.visibility = View.VISIBLE
                    binding.txtAppointmentDateTime.text = "Fecha: ${cita.appointmentDate} - Hora: ${cita.appointmentTime}"
                }
                else -> {
                    binding.txtAppointmentStatus.text = "Estado: Desconocido"
                    binding.txtAppointmentDateTime.visibility = View.GONE
                }
            }

            itemView.setOnClickListener { onItemClick(cita) }
        }
    }
}

