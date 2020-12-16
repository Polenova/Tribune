package ru.polenova.tribune.adapter

import android.view.View
import kotlinx.android.synthetic.main.post_card.view.*
import ru.polenova.tribune.postModel.Post

class AdViewHolder(
    adapter: PostAdapter,
    view: View,
    list: MutableList<Post>): PostViewHolder(adapter, view, list) {

    override fun bind(post: Post) {
        super.bind(post)
        with(view) {
            textViewPost.text = post.postText
        }
    }


}