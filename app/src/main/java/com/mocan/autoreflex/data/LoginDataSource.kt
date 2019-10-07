package com.mocan.autoreflex.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mocan.autoreflex.R
import com.mocan.autoreflex.ui.login.LoginResult
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(username: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(username, password)
    }

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout() {
        auth.signOut()
    }
}

