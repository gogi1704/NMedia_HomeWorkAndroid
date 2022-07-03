package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.viewModels.PostViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                textTitle.text = post.title
                textContent.text = post.content
                textDate.text = post.date
                textLikesCount.text = showCounts(post.likes)
                textShareCount.text = showCounts(post.shares)
                textShowsCount.text = showCounts(post.shows)
                val isLiked = if (post.isLiked) R.drawable.ic_like_true_24
                else R.drawable.ic_like_false_24
                imageLike.setImageResource(isLiked)

            }
        }

        binding.imageLike.setOnClickListener() {
            viewModel.like()
        }

        binding.imageShare.setOnClickListener {
            viewModel.share()
        }

    }

}


private fun showCounts(num: Int): String {
    val str: String = if (num < 1000) {
        "$num"
    } else if (num in 1000..9999) {
        val result = num / 1000
        val result2 = num / 100 % 10
        "${result}.${result2}K"
    } else if (num in 10_000..1_000_000) {
        val result = num / 1000
        "${result}K"
    } else {
        val result = num / 1_000_000
        val result2 = result % 10
        "${result}.${result2}M"
    }

    return str
}
