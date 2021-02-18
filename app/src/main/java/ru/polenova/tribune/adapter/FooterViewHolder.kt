package ru.polenova.tribune.adapter

import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_load_more.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.polenova.tribune.R
import ru.polenova.tribune.postModel.Repository
import java.io.IOException

class FooterViewHolder(private val adapter: PostAdapter, view: View) :
    RecyclerView.ViewHolder(view) {
    init {
        with(view) {
            buttonLoadMore.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        it.isEnabled = false
                        progressbarMore.visibility = View.VISIBLE
                        val lastItem = adapter.list.lastIndex
                        when  {
                            lastItem <= 0 -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.No_posts_before),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            lastItem > 0 -> {
                                val lastItemId = adapter.list[lastItem].idPost
                                val response = Repository.getPostsBefore(lastItemId)
                                if (response.isSuccessful) {
                                    val newItems = response.body()!!
                                    adapter.list.addAll(lastItem + 1, newItems)
                                    adapter.notifyItemRangeInserted(lastItem + 1, newItems.size)
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.loading_posts_failed,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                    } catch (e: IOException) {
                        Toast.makeText(
                            context,
                            R.string.connect_to_server_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        it.isEnabled = true
                        progressbarMore.visibility = View.GONE
                    }
                }
            }
        }
    }
}
