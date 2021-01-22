package ru.polenova.tribune

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.post_card.*
import ru.polenova.tribune.adapter.PostAdapter
import ru.polenova.tribune.postModel.Post

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

    }
}