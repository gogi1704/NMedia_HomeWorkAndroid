package com.example.nmedia.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.*
import com.example.nmedia.adapter.PostEventListener
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.FragmentMainBinding
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostViewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater, container, false)


        val recycler = binding.recyclerListPosts

        val adapter = PostsAdapter(
            object : PostEventListener {
                override fun like(post: Post) {
                    viewModel.like(post.id)
                }

                override fun clickItemShowPost(post: Post) {
                    findNavController()
                        .navigate(
                            R.id.action_mainFragment_to_showPostFragment,
                            createPostBundle(post)
                        )
                }

                override fun share(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    startActivity(intent)
                    viewModel.share(post.id)
                }

                override fun remove(post: Post) {
                    viewModel.remove(post.id)
                }

                override fun update(post: Post) {
                    findNavController()
                        .navigate(
                            R.id.action_mainFragment_to_editPostFragment,
                            createPostBundle(post)
                        )

                }

                override fun openVideo(post: Post) {
                    val webpage: Uri = Uri.parse(post.videoUri)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    startActivity(intent)
                }


            }
        )
        recycler.adapter = adapter
        binding.fabAddPost.setOnClickListener() {
            findNavController().navigate(R.id.action_mainFragment_to_createPostFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts.map { post -> post.copy() })
        }

        viewModel.editedLiveData.observe(viewLifecycleOwner) { editPost ->

            if (editPost.id == -1) {
                return@observe
            }

        }
        return binding.root
    }

    fun createPostBundle(post: Post): Bundle {
        return Bundle().apply {
            putInt(ID, post.id)
            putString(TITLE, post.title)
            putString(CONTENT, post.content)
            putString(DATE, post.date)
            putInt(LIKES, post.likes)
            putInt(SHARES, post.shares)
            putInt(SHOWS, post.shows)
            putString(URI, post.videoUri)
            putBoolean(ISLIKED, post.isLiked)

        }
    }

}



