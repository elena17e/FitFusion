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
                    val intent = Intent(this, LoginActivity2::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}


