package com.dgtic.practica2modulo6.ui.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dgtic.practica2modulo6.R
import com.dgtic.practica2modulo6.application.Practica2Modulo6App
import com.dgtic.practica2modulo6.data.LanguageRepository
import com.dgtic.practica2modulo6.databinding.FragmentLanguageDetailBinding
import com.dgtic.practica2modulo6.ui.MapActivity
import com.dgtic.practica2modulo6.utils.Constants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import java.io.IOException

private const val ID = "id"
private const val NAME = "name"

/**
 * A simple [Fragment] subclass.
 * Use the [LanguageDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LanguageDetailFragment : Fragment() {
    private var name: String? = null
    private var language: String? = null
    private var developer: String? = null
    private var lat: Double? = null
    private var long: Double? = null
    private var id: Int? = null

    private var _binding: FragmentLanguageDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LanguageRepository
    private lateinit var mediaPlayerError: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ID)
            name = it.getString(NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLanguageDetailBinding.inflate(inflater, container, false)

        mediaPlayerError = MediaPlayer.create(requireContext(), R.raw.error_sound)

        // Manda llamar al initRepository en VideogamesRFApp
        repository = (requireActivity().application as Practica2Modulo6App).repository

        // Ciclo de vida
        lifecycle.addObserver(binding.vidLanguaje)

        fillData()

        binding.btnRetry.setOnClickListener {
            fillData()
        }

        binding.imgMap.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            intent.putExtra("language", language)
            intent.putExtra("developer", developer)
            intent.putExtra("lat", lat)
            intent.putExtra("long", long)
            startActivity(intent)
        }

        return binding.root
    }

    private fun fillData() {
        lifecycleScope.launch {
            try {
                binding.btnRetry.visibility = View.VISIBLE
                binding.vidLanguaje.visibility = View.INVISIBLE
                binding.grdData.visibility = View.INVISIBLE
                binding.tvAttributes.visibility = View.INVISIBLE

                // Consume Servicio GET de cada lenaguaje
                val languageDetail = repository.getLanguageDetail(id)

                // Asigna los valores recuperados
                binding.apply {

                    lblName.text = name
                    lblDeveloper.text = languageDetail.co
                    lblReleased.text = languageDetail.year.toString()
                    lblCompiled.text = languageDetail.compiled
                    lblCrossPlatform.text = languageDetail.cross_platform
                    lblOOP.text = languageDetail.oop

                    language = name
                    developer = languageDetail.co
                    lat = languageDetail.lat
                    long = languageDetail.long

                    // Youtube video
                    vidLanguaje.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)

                            youTubePlayer.loadVideo(languageDetail.youtube.toString(), 0f )
                        }

                        override fun onError(
                            youTubePlayer: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            super.onError(youTubePlayer, error)
                            youTubePlayer.loadVideo(languageDetail.youtube.toString(), 0f )
                        }
                    })

                    Glide.with(requireContext())
                        .load(languageDetail.image_url)
                        .into(imgLanguage)

                    lblAttributes.text = lblAttributes.text.toString() + "\n"
                    for ( att in languageDetail.attributes!! )
                        lblAttributes.text = lblAttributes.text.toString() + "\n- " + att

                    binding.btnRetry.visibility = View.INVISIBLE
                    binding.vidLanguaje.visibility = View.VISIBLE
                    binding.grdData.visibility = View.VISIBLE
                    binding.tvAttributes.visibility = View.VISIBLE

                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
                Toast.makeText(requireActivity(),
                    getString(R.string.detail_conexion_err), Toast.LENGTH_LONG).show()
                binding.btnRetry.visibility = View.VISIBLE
                binding.vidLanguaje.visibility = View.INVISIBLE
                binding.grdData.visibility = View.INVISIBLE
                binding.tvAttributes.visibility = View.INVISIBLE

                // Sonido de error
                mediaPlayerError.start()
            } catch (e: Exception) {
                e.printStackTrace()

                // Sonido de error
                mediaPlayerError.start()
            } finally {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int, name: String) =
            LanguageDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, id)
                    putString(NAME, name)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Libera el youtube listener
        binding.vidLanguaje.release()
        mediaPlayerError.release()

        _binding = null
    }
}