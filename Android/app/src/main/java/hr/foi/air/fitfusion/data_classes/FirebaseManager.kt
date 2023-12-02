package hr.foi.air.fitfusion.data_classes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import hr.foi.air.fitfusion.LoginActivity
import hr.foi.air.fitfusion.WelcomeActivity
import hr.foi.air.fitfusion.databinding.ActivityLoginBinding

class FirebaseManager {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    fun loginUser(email: String, password: String, callback: (UserModel?, String?) -> Unit) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(UserModel::class.java)
                        callback(userData, null)
                        return
                    }
                } else {
                    callback(null, null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null, databaseError.message)
            }
        })
    }
}