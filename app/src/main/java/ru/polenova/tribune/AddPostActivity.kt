package ru.polenova.tribune

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.ktor.utils.io.errors.*
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.post_card.*
import kotlinx.coroutines.launch
import ru.polenova.tribune.postModel.Repository

class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        buttonCreatePost.setOnClickListener {
            val textPost = editTextPost.text.toString()
            val namePost = editTextNamePost.text.toString()
            val link = editTextAddLink.text.toString()
            when {
                textPost.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.text_empty), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    lifecycleScope.launch {
                        try {
                            val response = Repository.createPost(namePost, textPost, link)
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@AddPostActivity,
                                    getString(R.string.post_created),
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@AddPostActivity,
                                    getString(R.string.create_post_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: IOException) {
                            Toast.makeText(
                                this@AddPostActivity,
                                R.string.connect_to_server_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}