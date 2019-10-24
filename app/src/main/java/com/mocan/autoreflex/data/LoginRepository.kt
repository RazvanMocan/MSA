package com.mocan.autoreflex.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the FirebaseUser object
    var user: FirebaseUser? = null
        private set


    fun checkLoggedIn() {
        if(user == null && dataSource.isLoggedIn())
            user = dataSource.getUser()
    }

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = dataSource.getUser()
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Task<AuthResult> {
        // handle login
        val authTask = dataSource.login(username, password)

        authTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = dataSource.getUser()
                if (user != null)
                    setFirebaseUser(user)
            }
        }

        return authTask
    }

    private fun setFirebaseUser(FirebaseUser: FirebaseUser) {
        this.user = FirebaseUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    fun resetPassword(username: String):Task<Void> {
        return dataSource.resetPassword(username)
    }

    fun createUser(username: String, password: String): Task<AuthResult> {
        val createTask =  dataSource.createUser(username, password)
        createTask.addOnCompleteListener{task ->
            if (task.isSuccessful) {
                val user = dataSource.getUser()
                if (user != null)
                    setFirebaseUser(user)
            }
        }
        return createTask
    }
}
