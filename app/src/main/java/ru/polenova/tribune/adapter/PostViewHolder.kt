package ru.polenova.tribune.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_card.view.*
import ru.polenova.tribune.AboutPostActivity
import ru.polenova.tribune.R
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.Repository
import ru.polenova.tribune.postModel.StatusUser
import ru.polenova.tribune.postModel.Users

open class PostViewHolder(private val adapter: PostAdapter, val view: View, var list: MutableList<Post>) : RecyclerView.ViewHolder(view) {
    init {
        this.clickButtonListener()
    }
    open fun bind(post: Post) {
        with(view) {
            textViewUserName.text = post.userName
            textViewPost.text = post.postText
            textViewPostName.text = post.postName
            textViewData.text = post.dateOfCreate
            fillCount(textViewNumberUp, post.postUpCount)
            fillCount(textViewNumberDown, post.postDownCount)
            when {
                post.upActionPerforming -> {
                    imageViewUp.setImageResource(View.INVISIBLE)
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
                    imageViewDown.setImageResource(View.INVISIBLE)
                }
                post.pressedPostDown -> {
                    imageViewDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_red)
                }
                else -> {
                    imageViewDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_grey)
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
                    val item = adapter.list[adapterPosition]
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
                imageViewDown.setOnClickListener {
                    val currentPosition = adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.list[adapterPosition]
                        if (item.downActionPerforming) {
                            Toast.makeText(
                                context,
                                R.string.action_is_performing,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            adapter.downBtnClickListener?.onDownBtnClick(item, currentPosition)
                        }
                    }
                }
                imageViewLink.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.list[adapterPosition]
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(item.linkForPost)
                        }
                        itemView.context.startActivity(intent)
                    }
                }
                imageViewLook.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.list[adapterPosition]
                        val intent = Intent(context, AboutPostActivity::class.java)
                        intent.putExtra(item.userName, item.statusUser)
                        itemView.context.startActivity(intent)
                    }
                }
                imageViewClose.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val item: Post = adapter.list[adapterPosition]
                        adapter.notifyItemRemoved(adapterPosition)
                    }
                }
            }
        }
    }
}