package com.dgtic.practica2modulo6.ui.fragments

import android.media.MediaPlayer
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
import com.dgtic.practica2modulo6.data.remote.model.LanguageDto
import com.dgtic.practica2modulo6.databinding.FragmentLanguagesListBinding
import com.dgtic.practica2modulo6.ui.adapters.LanguagesAdapter
import com.dgtic.practica2modulo6.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * @author Carlo García Sánchez
 *
 * Fragment para mostrar la lista de Lenguajes de programación
 */
class LanguagesListFragment : Fragment() {
    private var _binding: FragmentLanguagesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LanguageRepository
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayerError: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLanguagesListBinding.inflate(inflater, container, false)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.click_sound)
        mediaPlayerError = MediaPlayer.create(requireContext(), R.raw.error_sound)

        binding.apply {
            btnRetry.setOnClickListener {
                // Consume servicio GET que lista los lenguajes de programación
                lifecycleScope.launch {
                    binding.pbLoading.visibility = View.VISIBLE

                    try {
                        val languages = repository.getLanguages()

                        // Llena los datos de los lenguajes
                        doDataBinding(languages)

                        binding.btnRetry.visibility = View.INVISIBLE
                    } catch (ioe: IOException) {
                        binding.btnRetry.visibility = View.VISIBLE
                        ioe.printStackTrace()
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.language_conection_error),
                            Toast.LENGTH_LONG
                        ).show()

                        // Sonido de error
                        mediaPlayerError.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.languages_error, e.message),
                            Toast.LENGTH_LONG
                        ).show()

                        // Sonido de error
                        mediaPlayerError.start()
                    } finally {
                        // Oculta el progressBar
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            }
    }

        return binding.root
    }

    // Fragment en pantalla
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Manda llamar al initRepository en Practica2Modulo6App
        repository = (requireActivity().application as Practica2Modulo6App).repository

        // Consume servicio GET que lista los lenguajes de programación
        lifecycleScope.launch {
            try {
                val languages = repository.getLanguages()

                // Llena los datos de los lenguajes
                doDataBinding(languages)

            } catch (ioe: IOException) {
                binding.btnRetry.visibility = View.VISIBLE
                ioe.printStackTrace()
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.language_conection_error),
                    Toast.LENGTH_LONG
                ).show()

                // Sonido de error
                mediaPlayerError.start()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.languages_error, e.message),
                    Toast.LENGTH_LONG
                ).show()

                // Sonido de error
                mediaPlayerError.start()
            } finally {
                // Oculta el progressBar
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun CoroutineScope.doDataBinding(languages: List<LanguageDto>?) {
        binding.lstLanguages.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = LanguagesAdapter(languages) { selectedLanguage ->
                Log.d(Constants.LOGTAG,
                    context.getString(R.string.lenguages_selected, selectedLanguage.name))

                // Al Click llama al fragment de detalle del juego
                selectedLanguage.id?.let { id ->
                    // Reproduce un sonido
                    mediaPlayer.start()

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
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.release()
        mediaPlayerError.release()

        _binding = null
    }
}