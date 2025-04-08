package com.dgtic.practica2modulo6.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgtic.practica2modulo6.data.remote.model.LanguageDto
import com.dgtic.practica2modulo6.databinding.ElementLanguageBinding

class LanguagesAdapter(
    private val languages: List<LanguageDto>, // Lenguaje
    private val onGameClick: (LanguageDto) -> Unit // Función de clic en un lenguaje
): RecyclerView.Adapter<LanguageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ElementLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language)

        holder.itemView.setOnClickListener {
            onGameClick(language)
        }
    }

    override fun getItemCount(): Int = languages.size
}