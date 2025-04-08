package com.dgtic.practica2modulo6.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dgtic.practica2modulo6.data.remote.model.LanguageDto
import com.dgtic.practica2modulo6.databinding.ElementLanguageBinding

class LanguageViewHolder(
    private val binding: ElementLanguageBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(language: LanguageDto) {
        // Vincula vistas con los datos del juego
        binding.lblName.text = language.name

        // Usando Glide para cargar la imagen
        Glide.with(binding.root.context)
            .load(language.image_url)
            .into(binding.imgThumbnail)
    }
}