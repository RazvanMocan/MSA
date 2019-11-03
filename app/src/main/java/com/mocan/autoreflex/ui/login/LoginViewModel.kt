package com.mocan.autoreflex.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.mocan.autoreflex.data.LoginRepository

import com.mocan.autoreflex.R


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _canReset = MutableLiveData<Boolean>()
    val canReset: LiveData<Boolean> = _canReset


    fun login(username: String, password: String) {
        val authTask = loginRepository.login(username, password)

        authTask.addOnCompleteListener{ task ->
            if (task.isSuccessful)
                _loginResult.value = LoginResult(success = loginRepository.user )
            else
                _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {

        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        _canReset.value = username.isEmpty()
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun alreadyLogged(): FirebaseUser? {
        loginRepository.checkLoggedIn()
        return loginRepository.user
    }

    fun logout() {
        loginRepository.logout()
    }

    fun resetPassword(username: String): Task<Void> {
        return loginRepository.resetPassword(username)
    }

    fun createUser(username: String, password: String): Task<AuthResult> {
        return loginRepository.createUser(username, password)
    }

    fun getActivity(): Task<GetTokenResult> {
        return loginRepository.userPersmissions()
    }
}
