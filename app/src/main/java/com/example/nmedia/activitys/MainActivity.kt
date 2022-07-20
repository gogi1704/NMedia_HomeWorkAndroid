package com.example.nmedia.activitys

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import com.example.nmedia.PostCreateContract
import com.example.nmedia.PostUpdateContract
import com.example.nmedia.adapter.PostEventListener
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.model.Post
import com.example.nmedia.viewModels.PostViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val recycler = binding.recyclerListPosts

        val postCreateLauncher = registerForActivityResult(PostCreateContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.editContent(result)
            viewModel.savePost()
        }

        val postUpdateLauncher = registerForActivityResult(PostUpdateContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.edit(result)
            viewModel.savePost()
        }


        val adapter = PostsAdapter(
            object : PostEventListener {
                override fun like(post: Post) {
                    viewModel.like(post.id)
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
                    postUpdateLauncher.launch(post)
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
            postCreateLauncher.launch()
        }

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts.map { post -> post.copy() })
        }

        viewModel.editedLiveData.observe(this) { editPost ->
            if (editPost.id == -1) {
                return@observe
            }

        }
    }
}



