package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : ComponentActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtCreateAcc: Button


    private lateinit var databaseRf: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtEmail = findViewById(R.id.loginEmail)
        txtPass= findViewById(R.id.loginPassword)
        btnLogin = findViewById(R.id.loginButton)
        txtCreateAcc = findViewById(R.id.createAcc)

        databaseRf = FirebaseDatabase.getInstance().getReference("user")

        btnLogin.setOnClickListener {
            loginUser()
        }
        txtCreateAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    data class UserModel(
        var usId: String?= null,
        var First_Name: String?=null,
        var Last_Name: String?=null,
        var Email: String?=null,
        var Password: String?=null
    )

    private fun loginUser() {
/*
        val Email = txtEmail.text.toString()
        val Password = txtPass.text.toString()


        val usId =   databaseRf

        val User = UserModel(Email, Password)

        databaseRf.child().setValue(User)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
*/
    }
}