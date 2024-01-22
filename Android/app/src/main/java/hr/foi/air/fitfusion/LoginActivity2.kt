package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser

import hr.foi.air.fitfusion.databinding.ActivityLoginBinding
import hr.foi.air.fitfusion.repositories.UserRepository
import hr.foi.air.google_login.GoogleLoginActivity

class LoginActivity2 : androidx.activity.ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var googleLogin: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        googleLogin = findViewById(R.id.textViewGoogle)
        userRepository = UserRepository(FirebaseManager())

        binding.loginButton.setOnClickListener {
            val loginEmail = binding.loginEmail.text.toString()
            val loginPassword = binding.loginPassword.text.toString()


            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                userRepository.loginUser(loginEmail, loginPassword) { user, _ ->
                    if (user != null) {
                        Toast.makeText(this@LoginActivity2, "Login successful!", Toast.LENGTH_SHORT)
                            .show()
                        loggedInUser = LoggedInUser(this)
                        loggedInUser.saveUserData(user.usId, user.firstName, user.lastName, loginPassword, loginEmail, user.type)
                        val intent: Intent = when (user?.type) {
                            "admin" -> Intent(this@LoginActivity2, WelcomeAdminActivity::class.java)
                            "trainer" -> Intent(this@LoginActivity2, WelcomeTrainerActivity::class.java)
                            "user" -> Intent(this@LoginActivity2, WelcomeActivity::class.java)
                            else -> {
                                Toast.makeText(this@LoginActivity2, "User type not recognized", Toast.LENGTH_SHORT).show()
                                return@loginUser
                            }
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity2,
                            "Wrong credentials!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@LoginActivity2,
                    "You must fill in all of the fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.createAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val showPassword = binding.showPasswordCheckBox
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.loginPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.loginPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
    }

    fun googleLogin(view: View) {
        val intent = Intent(this, GoogleLoginActivity::class.java)
        startActivity(intent)
    }

}
