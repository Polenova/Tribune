package ru.polenova.tribune

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.ktor.utils.io.errors.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.launch
import ru.polenova.tribune.api.Token
import ru.polenova.tribune.postModel.Repository
import ru.polenova.tribune.postModel.savedToken

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnRegister.setOnClickListener {
            val login = edtUserNameRegister.text.toString()
            val password1 = edtPasswordRegister.text.toString()
            val password2 = edtPassword2Register.text.toString()
            if (login == "" || password1 == "") {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show()
            } else if (password1 != password2) {
                Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    switchDeterminateBar(true)
                    try {
                        val response = Repository.register(login, password1)
                        if (response.isSuccessful) {
                            val token: Token? = response.body()
                            savedToken(token, this@RegistrationActivity)
                            Toast.makeText(this@RegistrationActivity,
                                getString(R.string.registration_completed),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegistrationActivity,
                                R.string.registration_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@RegistrationActivity,
                            getString(R.string.connect_to_server_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        switchDeterminateBar(false)
                    }
                }
            }
        }
        tvAuth.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun switchDeterminateBar(isLaunch: Boolean) {
        if (isLaunch) {
            determinateBarReg.visibility = View.VISIBLE
            btnRegister.isEnabled = false
        } else {
            determinateBarReg.visibility = View.GONE
            btnRegister.isEnabled = true
        }
    }
}