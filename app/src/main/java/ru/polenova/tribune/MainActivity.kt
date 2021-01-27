package ru.polenova.tribune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.ktor.utils.io.errors.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import ru.polenova.tribune.api.Token
import ru.polenova.tribune.postModel.Repository
import ru.polenova.tribune.postModel.savedToken
import ru.polenova.tribune.postModel.getToken

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAuth.setOnClickListener {
            val login = edtUserNameAuth.text.toString()
            val password = edtPasswordAuth.text.toString()
            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    switchDeterminateBar(true)
                    try {
                        val response = Repository.authenticate(login, password)
                        if (response.isSuccessful) {
                            val token: Token? = response.body()
                            savedToken(token, this@MainActivity)
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.authorization_completed),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivityIfAuthorized()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                R.string.authorization_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.authorization_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        switchDeterminateBar(false)
                    }
                }
            }
        }
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        startActivityIfAuthorized()
    }

    private fun switchDeterminateBar(isLaunch: Boolean) {
        if (isLaunch) {
            determinateBarMain.visibility = View.VISIBLE
            btnAuth.isEnabled = false
            tvRegister.isEnabled = false
        } else {
            determinateBarMain.visibility = View.GONE
            btnAuth.isEnabled = true
            tvRegister.isEnabled = true
            determinateBarMain.visibility = View.GONE
        }
    }
    private fun startActivityIfAuthorized() {
        val token = getToken(this)
        if (!token.isNullOrEmpty()) {
            Repository.createRetrofitWithAuth(token)
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}