package hr.foi.air.fitfusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hr.foi.air.fitfusion.ui.theme.FitFusionTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            setContentView(R.layout.activitiy_main)
            val buttonClick = findViewById<Button>(R.id.SignUp)
            buttonClick.setOnClickListener {
                setContentView(R.layout.activity_sign_up)
            }

            }
        }
    }

