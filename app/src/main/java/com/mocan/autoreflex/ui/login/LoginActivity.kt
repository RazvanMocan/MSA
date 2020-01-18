package com.mocan.autoreflex.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.main.MainMenuActivity
import com.mocan.autoreflex.ui.signup.SignUpActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this, NotificationReceiver::class.java))
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val signUp = findViewById<TextView>(R.id.textView2)
        val resetPassword = findViewById<TextView>(R.id.forgot_password)

        setProgressDialog()

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

            if (loginResult.error != null) {
                dialog.dismiss()
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                setResult(Activity.RESULT_OK)
            }
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
                    EditorInfo.IME_ACTION_DONE -> {

                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                    }

                }
                false
            }

            login.setOnClickListener {
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

    private fun setProgressDialog() {
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = getString(R.string.loading)
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setView(ll)

        dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams
        }
    }

    private fun alreadyLogged() {
        val loggedUser = loginViewModel.alreadyLogged()
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

    private fun changeActivity(activity: Class<*>, welcome:String, displayName:String?, type: String?) {
        val myIntent = Intent(this, activity)
        myIntent.putExtra("type", type)
        myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(myIntent)

        //Complete and destroy login activity once successful
        finish()

        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun updateUiWithUser(model: FirebaseUser) {
        dialog.show()
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName

        val activity = loginViewModel.getActivity()

        activity.addOnSuccessListener { result ->
            Log.e("type ", result.claims["type"].toString())

            changeActivity(
                MainMenuActivity::class.java,
                welcome,
                displayName,
                result.claims["type"] as String? ?: ""
            )
        }

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
