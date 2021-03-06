package com.example.mqttkotlinsample.data

import android.content.Context
import com.example.mqttkotlinsample.data.model.LoggedInUser
import java.io.IOException
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

import androidx.annotation.NonNull
import android.content.Context.KEYGUARD_SERVICE

import android.app.KeyguardManager



/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }


}