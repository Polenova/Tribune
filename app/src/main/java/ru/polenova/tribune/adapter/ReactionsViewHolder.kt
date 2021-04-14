package ru.polenova.tribune.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.reactions_card.view.*
import ru.polenova.tribune.R
import ru.polenova.tribune.UserActivity
import ru.polenova.tribune.postModel.Reaction
import ru.polenova.tribune.postModel.UsersReactionModel

class ReactionsViewHolder (
    private val adapter: ReactionAdapter, private val view: View, var list: MutableList<UsersReactionModel>
) : RecyclerView.ViewHolder(view) {

    init {
        this.clickButtonListener()
    }

    private fun clickButtonListener() {
        with(view) {
            imageViewUserAvatarReaction.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val userName = list[adapterPosition].userName
                    val intent = Intent(context, UserActivity::class.java)
                    intent.putExtra(PostViewHolder.USERNAME, userName)
                    context.startActivity(intent)
                }
            }

        }
    }

    fun bind(reactionModel: UsersReactionModel) {
        with (view) {

            textViewUserNameReaction.text = reactionModel.userName
            textViewStatusReaction.text = reactionModel.status.toString()
            textViewDataReaction.text = reactionModel.dateOfReaction

            when (reactionModel.reaction) {
                Reaction.UP ->
                    imageViewReaction.setImageResource(R.drawable.ic_baseline_thumb_up_24_green)
                Reaction.DOWN ->
                    imageViewReaction.setImageResource(R.drawable.ic_baseline_thumb_down_24_red)
            }
        }
    }
}