package com.mocan.autoreflex.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.firebase.auth.FirebaseUser

import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.main.MainMenuActivity
import com.mocan.autoreflex.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this, NotificationReceiver::class.java))
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val signUp = findViewById<TextView>(R.id.textView2)
        val resetPassword = findViewById<TextView>(R.id.forgot_password)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
        signUp.setOnClickListener {
            val myIntent = Intent(this, SignUpActivity::class.java)
            startActivity(myIntent)
        }

        resetPassword.setOnClickListener { resetPwd(username.text.toString()) }
        resetPassword.visibility = View.GONE
        loginViewModel.canReset.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult)
                resetPassword.visibility = View.GONE
            else
                resetPassword.visibility = View.VISIBLE
        })

    }

    private fun alreadyLogged() {
        val loggedUser = loginViewModel.alreadyLogged()
        Toast.makeText(applicationContext, "User: " + loggedUser.toString(), Toast.LENGTH_LONG)
            .show()
        if (loggedUser != null) {
            updateUiWithUser(loggedUser)
        }
    }

    private fun resetPwd(pasword: String) {
        val resetTask = loginViewModel.resetPassword(pasword)
        resetTask.addOnCompleteListener {
                task ->
            if (task.isSuccessful)
                Toast.makeText(applicationContext, "Reset email successfully sent",
                    Toast.LENGTH_LONG).show()
            else
                Toast.makeText(applicationContext, "Wrong email address", Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun changeActivity(activity: Class<*>, welcome:String, displayName:String?) {
        val myIntent = Intent(this, activity)
        startActivity(myIntent)

        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun updateUiWithUser(model: FirebaseUser) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName

        val activity = loginViewModel.getActivity()

        activity.addOnSuccessListener { result -> Log.e("type ", result.claims["type"].toString())
            when(result.claims["type"]) {
                "scoala" -> changeActivity(SignUpActivity::class.java, welcome, displayName)
                else -> changeActivity(MainMenuActivity::class.java, welcome, displayName)
            } }


    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        alreadyLogged()
        super.onStart()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
