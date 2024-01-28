package hr.foi.air.fitfusion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import androidx.core.app.NotificationManagerCompat
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
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            val intent = Intent(
                Settings.ACTION_APP_NOTIFICATION_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            startActivity(intent)
        }

        val channel = NotificationChannel(
            "trainingSignUp",
            "Sign up for Training",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notification for training applications"
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


