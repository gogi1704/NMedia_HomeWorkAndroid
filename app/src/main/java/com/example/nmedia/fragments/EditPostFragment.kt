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
                val post = viewModel.getPostById(requireArguments().getInt(ID))
                textTitle.text = post.author
                textContent.text = post.content
                textDate.text = post.published.toString()
                buttonLike.text = post.likes.toString()
                buttonShare.text = post.shares.toString()
                textShowsCount.text = post.shows.toString()
                buttonLike.isChecked =post.likedByMe

            with(createContent) {
                setText(post.content)
                requestFocus()
                AndroidUtils.showKeyboard(this)

            }


            fabSave.setOnClickListener() {
                val text = binding.createContent.text.toString()
                if (text != viewModel.getPostById(requireArguments().getInt(ID)).content) {
                    viewModel.edit(
                        Post(
                            id = requireArguments().getInt(ID),
                            author = textTitle.text.toString(),
                            published = textDate.text.toString().toInt(),
                            content = text,
                            likes = buttonLike.text.toString().toInt(),
                            shares = buttonShare.text.toString().toInt(),
                            shows = textShowsCount.text.toString().toInt(),
                            likedByMe = buttonLike.isChecked,
                            attachment = null
                        )
                    )
                    viewModel.savePost(parentFragmentManager)
                }
                findNavController().navigate(R.id.action_editPostFragment_to_mainFragment)

            }

            return binding.root
        }

    }

}