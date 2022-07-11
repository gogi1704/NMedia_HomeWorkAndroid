package com.example.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nmedia.R
import com.example.nmedia.databinding.ListItemLayoutBinding
import com.example.nmedia.model.Post


typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
) : ListAdapter<Post, PostsAdapter.PostsViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding =
            ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostsViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)

    }


    class PostsViewHolder(
        private val binding: ListItemLayoutBinding,
        private val onLikeListener: OnLikeListener,
        private val onShareListener: OnShareListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            with(binding) {
                textTitle.text = post.title
                textContent.text = post.content
                textLikesCount.text = post.showCounts(post.likes)
                textShareCount.text = post.showCounts(post.shares)
                textShowsCount.text = post.showCounts(post.shows)
                if (post.isLiked) imageLike.setImageResource(R.drawable.ic_like_true_24)
                else imageLike.setImageResource(R.drawable.ic_like_false_24)

                imageLike.setOnClickListener {
                    onLikeListener(post)
                }

                imageShare.setOnClickListener {
                    onShareListener(post)
                }
            }
        }
    }


}