package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import hr.foi.air.fitfusion.ui.theme.FitFusionTheme

class MainActivity : ComponentActivity() {
    private lateinit var btnSignUp: Button
    private lateinit var btnLogIn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitFusionTheme {
                setContentView(R.layout.activitiy_main)
                btnSignUp = findViewById(R.id.SignUp)
                btnLogIn = findViewById(R.id.LogIn)

                btnSignUp.setOnClickListener {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }

                btnLogIn.setOnClickListener {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getUserProfile() {
        // [START get_user_profile]
        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email

            //val type = it.uid

            //need to add function that checks the node which current user is under (Admin/Trainer/user)
            //accordingly to that node, write user type
        }
    }
}


