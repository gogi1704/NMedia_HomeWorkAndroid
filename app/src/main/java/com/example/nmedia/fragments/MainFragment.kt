package com.example.nmedia.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nmedia.*
import com.example.nmedia.adapter.PostEventListener
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.FragmentMainBinding
import com.example.nmedia.model.AttachmentType
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment(R.layout.fragment_main) {
    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private lateinit var binding: FragmentMainBinding
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadPost()
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        swipeToRefresh = binding.refreshSwipe
        val recycler = binding.recyclerListPosts

        val adapter = PostsAdapter(
            object : PostEventListener {
                override fun like(post: Post) {
                    viewModel.like(post.id, post.likedByMe)
                }

                override fun clickItemShowPost(post: Post) {
                    if (!post.isSendToServer) {
                        Snackbar.make(requireView(), "Send post to server ?", Snackbar.LENGTH_LONG)
                            .setAction("Send") {
                                viewModel.editContent(post.content)
                                viewModel.savePost()
                            }
                            .show()
                    } else {
                        findNavController()
                            .navigate(
                                R.id.action_mainFragment_to_showPostFragment,
                                createPostBundle(post)
                            )
                    }


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
                    if (post.attachment?.type == AttachmentType.VIDEO) {
                        val webpage: Uri = Uri.parse(post.attachment.url)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        startActivity(intent)
                    }

                }


            }
        )
        recycler.adapter = adapter
        binding.fabAddPost.setOnClickListener() {
            findNavController().navigate(R.id.action_mainFragment_to_createPostFragment)

        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            swipeToRefresh.isRefreshing = state.loading
            when (state.errorType) {
                ERROR_LOAD -> Snackbar
                    .make(
                        requireView(),
                        "Loading error! Swipe to refresh or click ->",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction("Retry") { viewModel.loadPost() }
                    .show()
                ERROR_REMOVE -> Snackbar
                    .make(requireView(), "Remove error.Please repeat", Snackbar.LENGTH_LONG)
                    .show()
                ERROR_LIKE -> Snackbar
                    .make(requireView(), "Like error.Please repeat", Snackbar.LENGTH_LONG)
                    .show()
            }


        }
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.textIsEmpty.isVisible = state.isEmpty
        }

        viewModel.errorCreateFragmentLiveData.observe(viewLifecycleOwner) {
            if (it) {
                MyDialogFragmentCreatePostError().show(parentFragmentManager, "create")
            }
        }


        viewModel.editedLiveData.observe(viewLifecycleOwner) { editPost ->

            if (editPost.id == -1L) {
                return@observe
            }

        }



        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            viewModel.loadPost()
        }

        return binding.root
    }

    private fun createPostBundle(post: Post): Bundle {
        println(post)
        return Bundle().apply {
            putLong(ID, post.id)
            putString(TITLE, post.author)
            putString(CONTENT, post.content)
            putString(DATE, post.published.toString())
            putLong(LIKES, post.likes)
            putLong(SHARES, post.shares)
            putLong(SHOWS, post.shows)
            // putString(URI, post.attachments)
            putBoolean(ISLIKED, post.likedByMe)

        }
    }


}



