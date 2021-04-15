package ru.polenova.tribune

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_reactions.*
import kotlinx.coroutines.launch
import ru.polenova.tribune.adapter.PostViewHolder.Companion.POST_ID
import ru.polenova.tribune.adapter.ReactionAdapter
import ru.polenova.tribune.postModel.Repository
import ru.polenova.tribune.postModel.UsersReactionModel
import java.io.IOException

class ReactionsActivity : AppCompatActivity() {

    private val idPost: Long by lazy { intent.getLongExtra(POST_ID, 0L) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reactions)
        setSupportActionBar(my_toolbar_reactions)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        lifecycleScope.launch {
            try {
                progressBarAboutPost.isEnabled = true
                val result = Repository.getReactionByUsers(idPost)
                if (result.isSuccessful) {
                    with(recyclerViewPostReactions) {
                        layoutManager = LinearLayoutManager(this@ReactionsActivity)
                        adapter = ReactionAdapter(
                            (result.body() ?: emptyList()) as MutableList<UsersReactionModel>
                        )
                    }
                } else {
                    Toast.makeText(
                        this@ReactionsActivity,
                        R.string.loading_posts_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@ReactionsActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBarAboutPost.isEnabled = true
            }
        }

        swipeContainerReactions.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        lifecycleScope.launch {
            try {
                val newData = Repository.getReactionByUsers(idPost)
                swipeContainerReactions.isRefreshing = false
                if (newData.isSuccessful) {
                    with(recyclerViewPostReactions) {
                        (adapter as ReactionAdapter).refresh(newData.body()!!)
                    }
                }
            } catch (e: IOException) {
                swipeContainerReactions.isRefreshing = false
                Toast.makeText(
                    this@ReactionsActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}
