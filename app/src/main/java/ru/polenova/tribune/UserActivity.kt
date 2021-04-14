package ru.polenova.tribune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.item_load_after_fail.*
import kotlinx.android.synthetic.main.post_card.*
import kotlinx.coroutines.launch
import ru.polenova.tribune.adapter.PostAdapter
import ru.polenova.tribune.adapter.PostViewHolder.Companion.USERNAME
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.PostDiffUtilCallback
import ru.polenova.tribune.postModel.Repository
import java.io.IOException

class UserActivity : AppCompatActivity(), PostAdapter.OnUpBtnClickListener,
    PostAdapter.OnDownBtnClickListener {

    val username: String by lazy { intent.getStringExtra(USERNAME)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(my_toolbar_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        lifecycleScope.launch {
            try {
                determinateBarUser.isVisible = true
                val result = if (username == getString(R.string.me)) {
                    Repository.getPostsOfMe()
                } else {
                    Repository.getPostsOfUser(username)
                }
                if (result.isSuccessful) {
                    with(containerUser) {
                        layoutManager = LinearLayoutManager(this@UserActivity)
                        adapter = PostAdapter(
                            (result.body() ?: emptyList()) as MutableList<Post>
                        ).apply {
                            upBtnClickListener = this@UserActivity
                            downBtnClickListener = this@UserActivity
                        }
                    }
                } else {
                    Toast.makeText(
                        this@UserActivity,
                        R.string.loading_posts_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@UserActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                determinateBarUser.isVisible = false
            }
        }
        swipeContainerUser.setOnRefreshListener {
            refreshData()
        }

    }

    private fun refreshData() {
        lifecycleScope.launch {
            try {
                val newData = if (username == getString(R.string.me)) {
                    Repository.getPostsOfMe()
                } else {
                    Repository.getPostsOfUser(username)
                }
                swipeContainer.isRefreshing = false
                if (newData.isSuccessful) {
                    with(containerUser) {
                        try {
                            val oldList = (adapter as PostAdapter).list
                            val newList = newData.body()!! as MutableList<Post>
                            val postDiffUtilCallback = PostDiffUtilCallback(oldList, newList)
                            val postDiffResult = DiffUtil.calculateDiff(postDiffUtilCallback)
                            (adapter as PostAdapter).newRecentPosts(newList)
                            postDiffResult.dispatchUpdatesTo(adapter as PostAdapter)
                        } catch (e: TypeCastException) {
                            layoutManager = LinearLayoutManager(this@UserActivity)
                            adapter = PostAdapter(
                                (newData.body() ?: emptyList()) as MutableList<Post>
                            ).apply {
                                upBtnClickListener = this@UserActivity
                                downBtnClickListener = this@UserActivity
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                swipeContainer.isRefreshing = false
                showDialogLoadAfterFail()
            }
        }
    }

    private fun showDialogLoadAfterFail() {
        val dialog = AlertDialog.Builder(this)
            .setView(R.layout.item_load_after_fail)
            .show()
        dialog.buttonTryElse.setOnClickListener {
            refreshData()
            dialog.dismiss()
        }
    }

    override fun onUpBtnClick(item: Post, position: Int) {
        lifecycleScope.launch {
            determinateBarUser.isVisible = true
            try {
                item.upActionPerforming = true
                with(containerUser) {
                    val response = Repository.pressPostUp(item.idPost)
                    adapter?.notifyItemChanged(position)
                    if (response.isSuccessful) {
                        item.updatePost(response.body()!!)
                    } else {
                        Toast.makeText(
                            this@UserActivity,
                            R.string.vote_only_once,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    item.upActionPerforming = false
                    adapter?.notifyItemChanged(position)
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@UserActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                determinateBarUser.isVisible = false
            }
        }
    }

    override fun onDownBtnClick(item: Post, position: Int) {
        lifecycleScope.launch {
            item.upActionPerforming = true
            try {
                determinateBarUser.isVisible = true
                with(recyclerViewPosts) {
                    adapter?.notifyItemChanged(position)
                    val response = Repository.pressPostDown(item.idPost)
                    if (response.isSuccessful) {
                        item.updatePost(response.body()!!)
                    } else {
                        Toast.makeText(
                            this@UserActivity,
                            R.string.vote_only_once,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    item.upActionPerforming = false
                    adapter?.notifyItemChanged(position)
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@UserActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                determinateBarUser.isVisible = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        R.id.action_my_posts -> {
            val intent = Intent(this, UserActivity::class.java)
            //intent.putExtra(PostViewHolder.USERNAME, getString(R.string.me))
            startActivity(intent)
            true
        }
        R.id.action_exit -> {
            startActivity(Intent(this, RegistrationActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

}
