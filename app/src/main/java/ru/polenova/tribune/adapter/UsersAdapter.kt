package ru.polenova.tribune.adapter

import android.content.Intent
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_card.view.*
import kotlinx.android.synthetic.main.users_card.view.*
import ru.polenova.tribune.AboutPostActivity
import ru.polenova.tribune.R
import ru.polenova.tribune.UserActivity
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.StatusUser
import ru.polenova.tribune.postModel.Users

class UsersAdapter (val listUsers: MutableList<Users>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.users_card, parent, false)
        return UsersAdapter.UsersViewHolder(this, view)
    }

    override fun getItemCount() = listUsers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as UsersAdapter.UsersViewHolder) {
            bind(listUsers[position])
        }
    }
    class UsersViewHolder(val adapter: UsersAdapter, view: View) : RecyclerView.ViewHolder(view) {
        init {
            with(itemView) {

                textViewUserNameReaction.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val item = adapter.listUsers[adapterPosition]
                        val intent = Intent(context, UserActivity::class.java)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }

        fun bind(users: Users) {
            with(itemView) {
                textViewUserNameReaction.text = users.userNameReaction
                textViewDataReaction.text = users.dateOfReaction

            }
        }
    }
}