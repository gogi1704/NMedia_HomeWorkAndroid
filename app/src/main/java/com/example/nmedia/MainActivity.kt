package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
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
        val adapter = PostsAdapter(
           object :PostEventListener{
               override fun like(post: Post) {
                  viewModel.like(post.id)
               }

               override fun share(post: Post) {
                  viewModel.share(post.id)
               }

               override fun remove(post: Post) {
                   viewModel.remove(post.id)
               }

               override fun update(post: Post) {
                  viewModel.edit(post)
               }
           }
        )
        recycler.adapter = adapter

        binding.buttonSend.setOnClickListener {
            if (binding.editTextContent.text.isNullOrBlank()) {
                Toast.makeText(this, "Text is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val text = binding.editTextContent.text.toString()

            viewModel.editContent(text)
            viewModel.savePost()
            binding.editTextContent.clearFocus()
            AndroidUtils.hideKeyboard(binding.editTextContent)
            binding.editTextContent.text = null

        }
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts.map { post -> post.copy() })
        }

        viewModel.editedLiveData.observe(this){editPost->
            if (editPost.id == 0){
                return@observe
            }else
            with(binding.editTextContent){
                requestFocus()
                setText(editPost.content)
            }
        }
    }
}



