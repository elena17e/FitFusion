package hr.foi.air.google_login.repositories

import hr.foi.air.google_login.data_classes_google.FirebaseManagerGoogle
import hr.foi.air.google_login.data_classes_google.UserModelGoogle

class UserRepositoryGoogle(private val firebaseManagerGoogle: FirebaseManagerGoogle) {

    fun loginUser(email: String, callback: (UserModelGoogle?, String?) -> Unit) {
        firebaseManagerGoogle.loginUserGoogle(email, callback)
    }
}