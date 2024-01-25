package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import android.widget.Toast
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser

import hr.foi.air.fitfusion.databinding.ActivityLoginBinding
import hr.foi.air.fitfusion.repositories.UserRepository
import hr.foi.air.google_login.GoogleLogin
import hr.foi.air.google_login.data_classes_google.FirebaseManagerGoogle
import hr.foi.air.google_login.repositories.UserRepositoryGoogle


class LoginActivity2 : androidx.activity.ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userRepositoryGoogle: UserRepositoryGoogle
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var googleLogin: TextView
    private lateinit var googleSignInHandler: GoogleLogin

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        googleLogin = findViewById(R.id.textViewGoogle)


        userRepository = UserRepository(FirebaseManager())
        userRepositoryGoogle = UserRepositoryGoogle(FirebaseManagerGoogle())

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
                        val intent: Intent = when (user.type) {
                            "admin" -> Intent(this@LoginActivity2, WelcomeAdminActivity::class.java)
                            "trainer" -> Intent(this@LoginActivity2, WelcomeTrainerActivity::class.java)
                            "user" -> Intent(this@LoginActivity2, WelcomeActivity::class.java)
                            else -> {
                                return@loginUser
                            }
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        googleSignInHandler = GoogleLogin(this).apply {
            setOnGoogleSignInSuccessListener { email ->
                userRepositoryGoogle.loginUser(email) { user, _ ->
                    if (user != null) {
                        loggedInUser = LoggedInUser(this@LoginActivity2)
                        user.password?.let {
                            loggedInUser.saveUserData(user.usId, user.firstName, user.lastName,
                                it, user.email, user.type)
                        }
                        val intent: Intent = when (user.type) {
                            "admin" -> Intent(this@LoginActivity2, WelcomeAdminActivity::class.java)
                            "trainer" -> Intent(this@LoginActivity2, WelcomeTrainerActivity::class.java)
                            "user" -> Intent(this@LoginActivity2, WelcomeActivity::class.java)
                            else -> {
                                return@loginUser
                            }
                        }
                        startActivity(intent)
                        finish()
                    }
                }
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
        binding.textViewGoogle.setOnClickListener {
            googleSignInHandler.signInGoogle()
        }
    }


    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleLogin.RC_SIGN_IN) {
            googleSignInHandler.handleSignInResult(resultCode, data)
        }
    }

}
