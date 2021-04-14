package ru.polenova.tribune.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_card.view.*
import ru.polenova.tribune.ReactionsActivity
import ru.polenova.tribune.R
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.StatusUser

open class PostViewHolder(
    private val adapter: PostAdapter,
    val view: View, var list: MutableList<Post>
) : RecyclerView.ViewHolder(view) {

    companion object {
        const val USERNAME = "USERNAME"
        const val POST_ID = "POST_ID"
    }

    init {
        this.clickButtonListener()
    }

    open fun bind(post: Post) {
        with(view) {
            textViewUserName.text = post.userName
            textViewPostName.text = post.postName
            textViewPost.text = post.postText
            textViewData.text = post.dateOfCreate
            fillCount(textViewNumberUp, post.postUpCount)
            fillCount(textViewNumberDown, post.postDownCount)
            when {
                post.link == null -> {
                    imageViewLink.visibility = View.GONE
                    imageViewLink.isEnabled = false
                }
                else -> {
                    imageViewLink.visibility = View.VISIBLE
                    imageViewLink.isEnabled = true
                }
            }
            when {
                post.upActionPerforming -> {
                    imageViewUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_white)
                }
                post.pressedPostUp -> {
                    imageViewUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_green)
                }
                else -> {
                    imageViewUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_grey)
                }
            }
            when {
                post.downActionPerforming -> {
                    imageViewDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_white)
                }
                post.pressedPostDown -> {
                    imageViewDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_red)
                }
                else -> {
                    imageViewDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_grey)
                }
            }
            when {
                post.statusUser == StatusUser.NONE -> {
                    textViewUserStatus.visibility = View.GONE
                }
            }
            if (post.statusUser == StatusUser.HATER) {
                textViewUserStatus.setTextColor(resources.getColor(R.color.colorRed))
                textViewUserStatus.text = context.getString(R.string.hater)
            }
            if (post.statusUser == StatusUser.PROMOTER) {
                textViewUserStatus.setTextColor(resources.getColor(R.color.colorGreen))
                textViewUserStatus.text = context.getString(R.string.promoter)
            }
        }
    }

    private fun fillCount(view: TextView, postCount: Int) {
        if (postCount == 0) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            view.text = postCount.toString()
        }
    }

    private fun clickButtonListener() {
        with(view) {
            imageViewUp.setOnClickListener {
                val currentPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = list[adapterPosition]
                    if (item.upActionPerforming) {
                        Toast.makeText(
                            context,
                            R.string.action_is_performing,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        adapter.upBtnClickListener?.onUpBtnClick(item, currentPosition)
                    }
                }
            }
            imageViewDown.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = list[adapterPosition]
                    if (item.downActionPerforming) {
                        Toast.makeText(
                            context,
                            R.string.action_is_performing,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        adapter.downBtnClickListener?.onDownBtnClick(item, adapterPosition)
                    }
                }
            }
            imageViewLink.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = list[currentPosition]
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(item.link)
                    }
                    itemView.context.startActivity(intent)
                }
            }
            imageViewLook.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val idPost = list[adapterPosition].idPost
                    val intent = Intent(context, ReactionsActivity::class.java)
                    intent.putExtra(POST_ID, idPost)
                    itemView.context.startActivity(intent)
                }
            }
            /*imageViewClose.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item: Post = list[currentPosition]
                    adapter.notifyItemRemoved()
                }
            }*/

        }
    }
}