package hr.foi.air.fitfusion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser

class UserProfile : AppCompatActivity() {
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var etUsName: EditText
    private lateinit var etUsSurname: EditText
    private lateinit var etUsEmail: EditText
    private lateinit var etUsPassword: EditText
    private lateinit var firebaseManager: FirebaseManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        firebaseManager = FirebaseManager()
        loggedInUser = LoggedInUser(this)
        etUsName = findViewById(R.id.editFirstName)
        etUsSurname = findViewById(R.id.editLastName)
        etUsEmail = findViewById(R.id.editEmail)
        etUsPassword = findViewById(R.id.editPassword)
        btnCancel = findViewById(R.id.buttonCancel)
        btnSave = findViewById(R.id.buttonSave)

        val firstName = loggedInUser.getFirstName()
        val lastName = loggedInUser.getLastName()
        val email = loggedInUser.getEmail()
        val password = loggedInUser.getPassword()
        val type = loggedInUser.getType()
        val usId = loggedInUser.getUserId()

        etUsName.setText(firstName)
        etUsSurname.setText(lastName)
        etUsEmail.setText(email)
        etUsPassword.setText(password)

        val showPasswordCheckBox: CheckBox = findViewById(R.id.showPasswordCheckBox)

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etUsPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                etUsPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        btnSave.setOnClickListener {
            val newPassword = etUsPassword.text.toString()
            if (isValidPassword(newPassword)) {
                firebaseManager.saveChangedPassword(email, newPassword, firstName, lastName, type, usId, this)
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(
                    this@UserProfile,
                    "Password needs to be at least 5 characters long and a combination of uppercase and lowercase letters, numbers and symbols",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()-+]).{5,}".toRegex()
        return passwordPattern.matches(password)
    }
}