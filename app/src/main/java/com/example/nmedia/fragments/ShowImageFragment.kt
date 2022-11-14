package com.example.nmedia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentShowImageBinding
import com.example.nmedia.viewModels.loadFitCenter
import com.example.nmedia.viewModels.loadImage

class ShowImageFragment : Fragment() {
    lateinit var binding: FragmentShowImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowImageBinding.inflate(layoutInflater, container, false)

        with(binding) {
            binding.image.loadFitCenter(requireArguments().getString("url").toString())
            exit.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        return binding.root
    }


}