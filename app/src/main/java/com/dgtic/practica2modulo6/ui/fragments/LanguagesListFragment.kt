package com.dgtic.practica2modulo6.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgtic.practica2modulo6.R
import com.dgtic.practica2modulo6.application.Practica2Modulo6App
import com.dgtic.practica2modulo6.data.LanguageRepository
import com.dgtic.practica2modulo6.databinding.FragmentLanguagesListBinding
import com.dgtic.practica2modulo6.ui.adapters.LanguagesAdapter
import com.dgtic.practica2modulo6.utils.Constants
import kotlinx.coroutines.launch

/**
 * @author Carlo García Sánchez
 *
 * Fragment para mostrar la lista de Lenguajes de programación
 */
class LanguagesListFragment : Fragment() {
    private var _binding: FragmentLanguagesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LanguageRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLanguagesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Frgment en pantalla
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Manda llamar al initRepository en Practica2Modulo6App
        repository = (requireActivity().application as Practica2Modulo6App).repository

        // Consume servicio GET que lista los lenguajes de programación
        lifecycleScope.launch {
            try {
                val languages = repository.getLanguages()
                binding.lstLanguages.apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = LanguagesAdapter(languages) { selectedLanguage ->
                        Log.d(Constants.LOGTAG, "Lenguaje seleccionado: ${selectedLanguage.name}")

                        // Al Click llama al fragment de detalle del juego
                        selectedLanguage.id?.let { id ->
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragment_container,
                                    LanguageDetailFragment.newInstance(
                                        id,
                                        selectedLanguage.name.toString()
                                    )
                                )
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireActivity(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                // Oculta el progressBar
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}