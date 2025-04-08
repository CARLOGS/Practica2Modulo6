package com.dgtic.practica2modulo6.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dgtic.practica2modulo6.application.Practica2Modulo6App
import com.dgtic.practica2modulo6.data.LanguageRepository
import com.dgtic.practica2modulo6.databinding.FragmentLanguageDetailBinding
import com.dgtic.practica2modulo6.utils.Constants
import kotlinx.coroutines.launch

private const val ID = "id"
private const val NAME = "name"

/**
 * A simple [Fragment] subclass.
 * Use the [LanguageDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LanguageDetailFragment : Fragment() {
    private var name: String? = null
    private var id: Int? = null

    private var _binding: FragmentLanguageDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: LanguageRepository

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

        // Manda llamar al initRepository en VideogamesRFApp
        repository = (requireActivity().application as Practica2Modulo6App).repository

        lifecycleScope.launch {
            try {
                // Consume Servicio GET de cada lenaguaje
                val languageDetail = repository.getLanguageDetail(id)

                // Asigna los valores recuperados
                binding.apply {
                    tvTitle.text = name

                    Glide.with(requireContext())
                        .load(languageDetail.image_url)
                        .into(ivImage)

                    tvLongDesc.text = languageDetail.attributes?.get(0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                binding.pbLoading.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(Constants.LOGTAG, "onViewCreated: $id")
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
        _binding = null
    }
}