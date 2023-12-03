package hr.foi.air.fitfusion.repositories

import com.google.firebase.auth.FirebaseUser
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.UserModel

class UserRepository(private val firebaseManager: FirebaseManager) {

    fun loginUser(email: String, password: String, callback: (UserModel?, String?) -> Unit) {
        firebaseManager.loginUser(email, password, callback)
    }
}