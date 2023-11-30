package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.UserData
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputBinding
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import hr.foi.air.fitfusion.databinding.ActivityLoginBinding
import javax.net.ssl.SSLSessionBindingEvent

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("user")


        binding.loginButton.setOnClickListener{
            val loginEmail = binding.loginEmail.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()){
                loginUser(loginEmail, loginPassword)
            }
            else{
                Toast.makeText(this@LoginActivity,"You must fill in all of the fields!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val showPassword = binding.showPasswordCheckBox
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.loginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }


    data class UserModel(
        var email: String?=null,
        var password: String?=null,
        var firstName: String?=null,
        var lastName: String?=null,
        var admin: Boolean?=false
    )

    private fun loginUser(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children){
                        val userData = userSnapshot.getValue(UserModel::class.java)

                        Toast.makeText(this@LoginActivity, "User login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                        intent.putExtra("USER_NAME", userData?.firstName)
                        intent.putExtra("IS_ADMIN", userData?.admin)
                        startActivity(intent)
                        finish()
                        return
                    }
                }
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}