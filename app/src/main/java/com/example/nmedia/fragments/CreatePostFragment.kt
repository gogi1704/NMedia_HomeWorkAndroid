package com.example.nmedia.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.AndroidUtils
import com.example.nmedia.databinding.FragmentCreatePostBinding
import com.example.nmedia.viewModels.PostViewModel

class CreatePostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCreatePostBinding.inflate(layoutInflater, container, false)
        val viewModel:PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        with(binding) {
            textEdit.requestFocus()
            AndroidUtils.showKeyboard(parentConstraint)
        }

        binding.fbSavePost.setOnClickListener {
            val text = binding.textEdit.text.toString()
            if (binding.textEdit.text.isNotBlank()) {

                viewModel.editContent(text)
                viewModel.savePost()
                findNavController().navigateUp()

            } else findNavController().navigateUp()

        }

        return binding.root
    }


}

