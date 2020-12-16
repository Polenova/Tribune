package ru.polenova.tribune.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_card.view.*
import org.json.JSONArray
import ru.polenova.tribune.R
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.Repository
import ru.polenova.tribune.postModel.StatusUser
import ru.polenova.tribune.postModel.Users
import ru.polenova.tribune.AboutPostActivity as AboutPostActivity1

class PostAdapter(var list: MutableList<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var upBtnClickListener: OnUpBtnClickListener? = null
    var downBtnClickListener: OnDownBtnClickListener? = null

    fun newRecentPosts(newData: List<Post>) {
        this.list.clear()
        this.list.addAll(newData)
    }

    interface OnUpBtnClickListener {
        fun onUpBtnClick(item: Post, position: Int)
    }

    interface OnDownBtnClickListener {
        fun onDownBtnClick(item: Post, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.post_card, parent, false)
        return PostViewHolder(this, view, list)
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as PostViewHolder) {
            bind(list[position])
        }
    }


}
