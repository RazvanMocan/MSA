package com.mocan.autoreflex.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

    fun resetPassword(username: String):Task<Void> {
        return auth.sendPasswordResetEmail(username)
    }

    fun createUser(username: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(username, password)
    }
}

