package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(0, "Netology", "22.04.2022", " TRA TA TA TA Ta", 12999, 120999, 999_999)
        with(binding) {
            textTitle.text = post.title
            textContent.text = post.content
            textDate.text = post.date
            textLikesCount.text = showCounts(post.likes)
            textShareCount.text = showCounts(post.shares)
            textShowsCount.text = showCounts(post.shows)

            imageLike.setOnClickListener() {
                likePost(post)
            }

            binding.imageShare.setOnClickListener {
                post.shares++
                binding.textShareCount.text = showCounts(post.shares)
            }

        }


    }

    private fun likePost(post: Post) {
        if (!post.isLiked) {
            post.likes++
            binding.imageLike.setImageResource(R.drawable.ic_like_true_24)
            binding.textLikesCount.text = showCounts(post.likes)
            post.isLiked = true
        } else if (post.isLiked) {
            post.likes--
            binding.imageLike.setImageResource(R.drawable.ic_like_false_24)
            binding.textLikesCount.text = showCounts(post.likes)
            post.isLiked = false
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
