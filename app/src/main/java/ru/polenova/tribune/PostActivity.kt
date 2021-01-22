package ru.polenova.tribune

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_post.*
import ru.polenova.tribune.adapter.PostAdapter
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.Repository
import java.io.IOException
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_load_after_fail.*
import kotlinx.coroutines.launch
import ru.polenova.tribune.postModel.PostDiffUtilCallback

class PostActivity : AppCompatActivity(), PostAdapter.OnUpBtnClickListener,
    PostAdapter.OnDownBtnClickListener {

    private companion object {
        const val CREATE_POST_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        requestToken()

        fab.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivityForResult(intent, CREATE_POST_REQUEST_CODE)
        }

        lifecycleScope.launch {
            try {
                switchDeterminateBar(true)
                val result = Repository.getRecent()
                if (result.isSuccessful) {
                    with(recyclerViewPosts) {
                        layoutManager = LinearLayoutManager(this@PostActivity)
                        adapter = PostAdapter(
                            (result.body() ?: emptyList()) as MutableList<Post>
                        ).apply {
                            upBtnClickListener = this@PostActivity
                            downBtnClickListener = this@PostActivity
                        }
                    }
                } else {
                    Toast.makeText(
                        this@PostActivity,
                        R.string.loading_posts_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@PostActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                switchDeterminateBar(false)
            }
        }
        swipeContainer.setOnRefreshListener {
            refreshData()
        }

    }

    private fun refreshData() {
        lifecycleScope.launch {
            try {
                val newData = Repository.getRecent()
                swipeContainer.isRefreshing = false
                if (newData.isSuccessful) {
                    with(recyclerViewPosts) {
                        try {
                            val oldList = (adapter as PostAdapter).list
                            val newList = newData.body()!! as MutableList<Post>
                            val postDiffUtilCallback = PostDiffUtilCallback(oldList, newList)
                            val postDiffResult = DiffUtil.calculateDiff(postDiffUtilCallback)
                            (adapter as PostAdapter).newRecentPosts(newList)
                            postDiffResult.dispatchUpdatesTo(adapter as PostAdapter)
                        } catch (e: TypeCastException) {
                            layoutManager = LinearLayoutManager(this@PostActivity)
                            adapter = PostAdapter(
                                (newData.body() ?: emptyList()) as MutableList<Post>
                            ).apply {
                                upBtnClickListener = this@PostActivity
                                downBtnClickListener = this@PostActivity
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CREATE_POST_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    try {
                        switchDeterminateBar(true)
                        val result = Repository.getRecent()
                        if (result.isSuccessful) {
                            with(recyclerViewPosts) {
                                val oldList = (adapter as PostAdapter).list
                                val newList = result.body()!! as MutableList<Post>
                                val postDiffUtilCallback = PostDiffUtilCallback(oldList, newList)
                                val postDiffResult = DiffUtil.calculateDiff(postDiffUtilCallback)
                                (adapter as PostAdapter).newRecentPosts(newList)
                                postDiffResult.dispatchUpdatesTo(adapter as PostAdapter)
                            }
                        } else {
                            Toast.makeText(
                                this@PostActivity,
                                R.string.loading_posts_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@PostActivity,
                            R.string.connect_to_server_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        switchDeterminateBar(false)
                    }
                }
            }
        }
    }

    override fun onUpBtnClick(item: Post, position: Int) {
        lifecycleScope.launch {
            switchDeterminateBar(true)
            try {
                item.upActionPerforming = true
                with(recyclerViewPosts) {
                    adapter?.notifyItemChanged(position)
                    val response = if (item.pressedPostUp) {
                        Toast.makeText(
                            this@PostActivity,
                            R.string.vote_only_once,
                            Toast.LENGTH_SHORT
                        ).show()
                        null
                    } else {
                        Repository.pressedPostUp(item.idPost)
                    }
                    item.upActionPerforming = false
                    if (response != null && response.isSuccessful) {
                        item.updatePost(response.body()!!)
                    }
                    adapter?.notifyItemChanged(position)
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@PostActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                switchDeterminateBar(false)
            }
        }
    }

    override fun onDownBtnClick(item: Post, position: Int) {
        lifecycleScope.launch {
            switchDeterminateBar(true)
            try {
                //item.downActionPerforming = true
                with(recyclerViewPosts) {
                    adapter?.notifyItemChanged(position)
                    val response = if (item.pressedPostDown) {
                        Repository.pressedPostDownRemove(item.idPost)
                    } else {
                        Repository.pressedPostDown(item.idPost)
                    }
                    //item.downActionPerforming = false
                    if (response.isSuccessful) {
                        item.updatePost(response.body()!!)
                    }
                    adapter?.notifyItemChanged(position)
                }
            } catch (e: IOException) {
                Toast.makeText(
                    this@PostActivity,
                    R.string.connect_to_server_failed,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                switchDeterminateBar(false)
            }
        }
    }

    private fun switchDeterminateBar(isLaunch: Boolean) {
        if (isLaunch) {
            progressBarPosts.visibility = View.VISIBLE
            fab.isEnabled = false

        } else {
            progressBarPosts.visibility = View.GONE
            fab.isEnabled = true
        }
    }


    /*override fun onDestroy() {
        super.onDestroy()
        if (isFirstTime(this)) {
            NotificationHelper.sayGoodbye(this)
            setNotFirstTime(this)
        }
    }*/

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@PostActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }

            if (isUserResolvableError(code)) {
                getErrorDialog(this@PostActivity, code, 9000).show()
                return
            }

            Snackbar.make(
                swipeContainer,
                getString(R.string.google_play_unavailable),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
    }

    private fun showDialogLoadAfterFail() {
        val dialog = AlertDialog.Builder(this)
            .setView(R.layout.item_load_after_fail)
            .show()
        dialog.buttonTryElse.setOnClickListener {
            requestToken()
            dialog.dismiss()
        }
    }

}