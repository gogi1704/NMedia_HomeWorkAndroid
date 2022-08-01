package com.example.nmedia.fragments

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.*
import com.example.nmedia.databinding.FragmentShowPostBinding
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostViewModel


class ShowPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowPostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)


        with(binding) {
            val post = viewModel.getPostById(requireArguments().getInt(ID))
            textTitle.text = post.title
            textContent.text = post.content
            textDate.text = post.date
            buttonLike.text = post.likes.toString()
            buttonShare.text = post.shares.toString()
            textShowsCount.text = post.shows.toString()
            buttonLike.isChecked =post.isLiked

            buttonLike.setOnClickListener{
                val likes:Int = if (buttonLike.isChecked) buttonLike.text.toString().toInt() + 1
                else buttonLike.text.toString().toInt() - 1
                buttonLike.text = likes.toString()
                viewModel.like( requireArguments().getInt(ID))

            }

            buttonShare.setOnClickListener{
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,textContent.toString())
                    type = "text/plain"
                }
                startActivity(intent)
                val shares = buttonShare.text.toString().toInt()+1
                buttonShare.text = shares.toString()
                viewModel.share( requireArguments().getInt(ID))
            }

            buttonMore.setOnClickListener(){
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_more)

                    setOnMenuItemClickListener { itemView ->
                        when (itemView.itemId) {
                            R.id.remove -> {
                                viewModel.remove(requireArguments().getInt(ID))
                                findNavController().navigateUp()
                                return@setOnMenuItemClickListener true

                            }
                            R.id.update -> {
                                findNavController().navigate(R.id.action_showPostFragment_to_editPostFragment , Bundle().apply {
                                    putInt(ID, requireArguments().getInt(ID))
                                    })

                                return@setOnMenuItemClickListener true
                            }
                        }
                        false
                    }
                }.show()
            }
        }









        return binding.root
    }


}