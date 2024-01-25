package hr.foi.air.google_login.data_classes_google

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseManagerGoogle {

    private var firebaseDatabase: FirebaseDatabase
    private var databaseReference: DatabaseReference

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")
    }

    fun loginUserGoogle(email: String, callback: (UserModelGoogle?, String?) -> Unit) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")

        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @SuppressLint("ObsoleteSdkInt")
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for (userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(UserModelGoogle::class.java)
                        if (userData != null) {
                            callback(userData, null)
                        } else {
                            callback(null, "Invalid email or password")
                        }
                        return
                    }
                } else {
                    callback(null, "Invalid email or password")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null, databaseError.message)
            }
        })
    }
}