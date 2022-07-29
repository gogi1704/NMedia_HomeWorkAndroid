package com.example.nmedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.*
import com.example.nmedia.databinding.FragmentEditPostBinding
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostViewModel


class EditPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        with(binding) {
            textTitle.text = requireArguments().getString(TITLE)
            textContent.text = requireArguments().getString(CONTENT)
            textDate.text = requireArguments().getString(DATE)
            buttonLike.text = requireArguments().getInt(LIKES).toString()
            buttonShare.text = requireArguments().getInt(SHARES).toString()
            textShowsCount.text = requireArguments().getInt(SHOWS).toString()
            buttonLike.isChecked = requireArguments().getBoolean(ISLIKED)

            with(createContent) {
                setText(arguments?.getString(CONTENT))
                requestFocus()
                AndroidUtils.showKeyboard(this)

            }


            fabSave.setOnClickListener() {
                val text = binding.createContent.text.toString()
                if (text != requireArguments().getString(CONTENT)) {

                    viewModel.edit(
                        Post(
                            id = requireArguments().getInt(ID),
                            title = textTitle.text.toString(),
                            date = textDate.text.toString(),
                            content = text,
                            likes = buttonLike.text.toString().toInt(),
                            shares = buttonShare.text.toString().toInt(),
                            shows = textShowsCount.text.toString().toInt(),
                            isLiked = buttonLike.isChecked
                        )
                    )
                    viewModel.savePost()
                    findNavController().navigate(R.id.action_editPostFragment_to_mainFragment)

                } else findNavController().navigate(R.id.action_editPostFragment_to_mainFragment)


            }

            return binding.root
        }

    }

}