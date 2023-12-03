package hr.foi.air.fitfusion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.core.app.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.UserModel
import hr.foi.air.fitfusion.databinding.ActivityLoginBinding
import hr.foi.air.fitfusion.repositories.UserRepository

class LoginActivity2 : androidx.activity.ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(FirebaseManager())

        binding.loginButton.setOnClickListener {
            val loginEmail = binding.loginEmail.text.toString()
            val loginPassword = binding.loginPassword.text.toString()


            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                userRepository.loginUser(loginEmail, loginPassword) { user, error ->
                    if (user != null) {
                        Toast.makeText(this@LoginActivity2, "Login successful!", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@LoginActivity2, WelcomeActivity::class.java)
                        intent.putExtra(
                            "USER_NAME",
                            user.firstName
                        )
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
        showPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.loginPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.loginPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
    }
}
