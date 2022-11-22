package com.example.nmedia.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.nmedia.*
import com.example.nmedia.adapter.PostEventListener
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.FragmentMainBinding
import com.example.nmedia.model.AttachmentType
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.AuthViewModel
import com.example.nmedia.viewModels.PostViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val postViewModel: PostViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postViewModel.loadPost()
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        swipeToRefresh = binding.refreshSwipe
        val recycler = binding.recyclerListPosts

        val adapter = PostsAdapter(
            object : PostEventListener {
                override fun like(post: Post) {
                    if (authViewModel.authenticated) {
                        postViewModel.like(post.id, post.likedByMe)
                    } else {
                        Snackbar.make(
                            requireView(),
                            "Please pass authorization",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Go") {
                                findNavController().navigate(R.id.action_mainFragment_to_authFragment)
                            }
                            .show()
                    }

                }

                override fun clickItemShowPost(post: Post) {
                    if (!post.isSendToServer) {
                        Snackbar.make(requireView(), "Send post to server ?", Snackbar.LENGTH_LONG)
                            .setAction("Send") {
                                postViewModel.savePostAfterError(post)
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
                    postViewModel.share(post.id)
                }

                override fun remove(post: Post) {
                    postViewModel.remove(post.id)
                }

                override fun update(post: Post) {
                    findNavController()
                        .navigate(
                            R.id.action_mainFragment_to_editPostFragment,
                            createPostBundle(post)
                        )

                }

                override fun openAttachment(post: Post) {
                    when (post.attachment?.type) {
                        AttachmentType.VIDEO -> {
                            val webpage: Uri = Uri.parse(post.attachment.url)
                            val intent = Intent(Intent.ACTION_VIEW, webpage)
                            startActivity(intent)
                        }
                        AttachmentType.IMAGE -> {
                            findNavController().navigate(
                                R.id.action_mainFragment_to_showImageFragment,
                                Bundle().apply {
                                    putString("url", post.attachment.url)
                                })
                        }
                        else -> {}
                    }

                }


            }
        )
        recycler.adapter = adapter



        binding.fabAddPost.setOnClickListener() {
            if (authViewModel.authenticated) {
                findNavController().navigate(R.id.action_mainFragment_to_createPostFragment)
            } else {
                Snackbar.make(requireView(), "Please pass authorization", Snackbar.LENGTH_LONG)
                    .setAction("Go") {
                        findNavController().navigate(R.id.action_mainFragment_to_authFragment)
                    }
                    .show()
            }
        }

        binding.buttonUncheckedPosts.setOnClickListener {
            binding.buttonUncheckedPosts.visibility = View.GONE
            postViewModel.checkPosts()
            recycler.smoothScrollToPosition(0)
        }


        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            swipeToRefresh.isRefreshing = state.loading
            when (state.errorType) {
                ERROR_LOAD -> Snackbar
                    .make(
                        requireView(),
                        "Loading error! Swipe to refresh or click ->",
                        Snackbar.LENGTH_LONG
                    )
                    .setAction("Retry") { postViewModel.loadPost() }
                    .show()
                ERROR_REMOVE -> Snackbar
                    .make(
                        requireView(),
                        "Remove error. Check network and repeat",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
                ERROR_LIKE -> Snackbar
                    .make(
                        requireView(),
                        "Like error. Check network and repeat",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
                ERROR_SAVE -> Snackbar
                    .make(
                        requireView(),
                        "Save error. Check network and repeat",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
                ERROR_REPEAT_REQUEST -> Snackbar
                    .make(
                        requireView(),
                        "Repeat request error. Check network and repeat",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }

        }



        postViewModel.newerCount.observe(viewLifecycleOwner) {
            with(binding) {
                if (it != 0) {
                    buttonUncheckedPosts.visibility = View.VISIBLE
                    getString(R.string.To_new_posts) + " ( $it )"
                }
            }
        }

        postViewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.textIsEmpty.isVisible = state.isEmpty
        }


        postViewModel.errorCreateFragmentLiveData.observe(viewLifecycleOwner) {
            if (it) {
                MyDialogFragmentCreatePostError().show(parentFragmentManager, "create")
            }
        }


        postViewModel.editedLiveData.observe(viewLifecycleOwner) { editPost ->

            if (editPost.id == -1L) {
                return@observe
            }

        }



        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = false
            postViewModel.loadPost()
        }

        return binding.root
    }

    private fun createPostBundle(post: Post): Bundle {
        return Bundle().apply {
            putLong(ID, post.id)
            putLong(AUTHOR_ID, post.authorId)
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



