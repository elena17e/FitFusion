package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.data_classes.UserModel
import java.security.MessageDigest
import java.security.SecureRandom

class SignUpActivity : ComponentActivity() {
    private lateinit var etUsName: EditText
    private lateinit var etUsSurname: EditText
    private lateinit var etUsEmail: EditText
    private lateinit var etUsPassword: EditText
    private lateinit var btnDone: Button
    private lateinit var btnReturn: Button
    private lateinit var databaseRf: DatabaseReference
    private lateinit var loggedInUser: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        etUsName = findViewById(R.id.FirstName)
        etUsSurname = findViewById(R.id.LastName)
        etUsEmail = findViewById(R.id.Email)
        etUsPassword = findViewById(R.id.Password)
        btnDone = findViewById(R.id.Done)
        btnReturn = findViewById(R.id.Cancel)

        databaseRf = FirebaseDatabase.getInstance().getReference("user")

        btnDone.setOnClickListener {
            val firstName = etUsName.text.toString()
            val lastName = etUsSurname.text.toString()
            val email = etUsEmail.text.toString()
            val password = etUsPassword.text.toString()
            val type = "user"


            checkIfEmailExists(email) { exists ->
                if (exists) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "User with the same email already exists",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (isValidPassword(password)) {
                        val userId = databaseRf.push().key!!

                        val salt = generateSalt()
                        val hashedPassword = hashPassword(password, salt)

                        val user = UserModel(email, password, hashedPassword, salt, firstName, lastName, type, userId)

                        databaseRf.child(userId).setValue(user)
                            .addOnCompleteListener {
                                loggedInUser = LoggedInUser(this)
                                loggedInUser.saveUserData(userId, firstName, lastName, password, email, type)
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Registration complete",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this@SignUpActivity, WelcomeActivity::class.java)
                                startActivity(intent)
                                finish()

                            }.addOnFailureListener { err ->
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Error ${err.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Password needs to be at least 5 characters long and a combination of uppercase and lowercase letters, numbers and symbols",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val showPasswordCheckBox: CheckBox = findViewById(R.id.showPasswordCheckBox)

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etUsPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                etUsPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

    }
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()-+]).{5,}".toRegex()
        return passwordPattern.matches(password)
    }
    private fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {
        databaseRf.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }
    private fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPassword(password: String, salt: String): String {
        try {
            val bytes = (password + salt).toByteArray(Charsets.UTF_8)
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            throw RuntimeException("Error hashing password", e)
        }
    }
}
