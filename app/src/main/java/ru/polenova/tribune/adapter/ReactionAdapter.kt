package ru.polenova.tribune.adapter

import android.system.Os.bind
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.polenova.tribune.R
import ru.polenova.tribune.postModel.UsersReactionModel

class ReactionAdapter(var list: MutableList<UsersReactionModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun refresh(newData: List<UsersReactionModel>) {
        this.list.clear()
        this.list.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.reactions_card, parent, false)
        return ReactionsViewHolder(this, view, list)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val UsersReactionModel = list[position]
        with(holder as ReactionsViewHolder) {
            bind(UsersReactionModel)
        }
    }
}
