package com.example.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nmedia.R
import com.example.nmedia.databinding.ListItemLayoutBinding
import com.example.nmedia.model.Post

interface PostEventListener {
    fun like(post: Post)
    fun share(post: Post)
    fun remove(post: Post)
    fun update(post: Post)
    fun openVideo(post: Post)
    fun clickItemShowPost(post: Post)
}




class PostsAdapter(
    private val listener: PostEventListener

) : ListAdapter<Post, PostsAdapter.PostsViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding =
            ListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostsViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)


    }


    class PostsViewHolder(

        private val binding: ListItemLayoutBinding,
        private val listener: PostEventListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            with(binding) {
                if (post.videoUri != null) {
                    videoContent.visibility = View.VISIBLE
                    textTitleVideo.text = "Test Video"
                }
                textTitle.text = post.author
                textContent.text = post.content
                textDate.text = post.published.toString()
                buttonLike.text = post.showCounts(post.likes)
                buttonShare.text = post.showCounts(post.shares)
                textShowsCount.text = post.showCounts(post.shows)
                buttonLike.isChecked = post.likedByMe

                imageVideo.setOnClickListener {
                    listener.openVideo(post)
                }

                buttonLike.setOnClickListener {
                    listener.like(post)
                }

                buttonShare.setOnClickListener {
                    listener.share(post)
                }

                itemView.setOnClickListener{
                    clickItem(post)
                }



                buttonMore.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_more)

                        setOnMenuItemClickListener { itemView ->
                            when (itemView.itemId) {
                                R.id.remove -> {
                                    listener.remove(post)
                                    return@setOnMenuItemClickListener true
                                }
                                R.id.update -> {
                                    listener.update(post)
                                    return@setOnMenuItemClickListener true
                                }
                            }
                            false
                        }
                    }.show()
                }
            }
        }

        private fun clickItem(post: Post){
            listener.clickItemShowPost(post)
        }
    }


}