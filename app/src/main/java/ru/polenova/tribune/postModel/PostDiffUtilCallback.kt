package ru.polenova.tribune.postModel

import androidx.recyclerview.widget.DiffUtil

class PostDiffUtilCallback(
    private val oldList: MutableList<Post>,
    private val newList: MutableList<Post>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.idPost == newModel.idPost
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.postText == newModel.postText
                && oldModel.postUpCount == newModel.postUpCount
                && oldModel.postDownCount == newModel.postDownCount
                && oldModel.statusUser == newModel.statusUser
    }

}