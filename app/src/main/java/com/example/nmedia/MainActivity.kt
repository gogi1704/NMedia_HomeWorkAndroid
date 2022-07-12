package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.nmedia.adapter.PostsAdapter
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.viewModels.PostViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val recycler = binding.recyclerListPosts
        val adapter = PostsAdapter(
            { viewModel.like(it.id) },
            { viewModel.share(it.id) }
        )
        recycler.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts.map { post -> post.copy() })
        }
    }
}



