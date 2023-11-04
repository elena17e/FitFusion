package hr.foi.air.fitfusion

import android.os.Bundle
import android.content.Intent
import android.os.UserManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import hr.foi.air.fitfusion.ui.theme.FitFusionTheme

class SignUpActivity : ComponentActivity() {
    private lateinit var etUsName: EditText
    private lateinit var etUsSurname: EditText
    private lateinit var etUsEmail: EditText
    private lateinit var etUsPassword: EditText
    private lateinit var btnSaveData: Button


    private lateinit var databaseRf: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_sign_up)
            etUsName = findViewById(R.id.FirstName)
            etUsSurname = findViewById(R.id.LastName)
            etUsEmail = findViewById(R.id.Email)
            etUsPassword = findViewById(R.id.Password)
            val btnReturn = findViewById<Button>(R.id.Cancel)


            databaseRf = FirebaseDatabase.getInstance().getReference("user")

            btnSaveData.setOnClickListener {
                saveEmployeeData()
            }
            btnReturn.setOnClickListener {
                setContentView(R.layout.activitiy_main)
            }
        }
    }


    data class UserModel(
        var usId: String?= null,
        var usName: String?=null,
        var usSurname: String?=null,
        var usEmail: String?=null,
        var usPassword: String?=null
    )

    private fun saveEmployeeData() {

        //getting values
        val usName = etUsName.text.toString()
        val usSurname = etUsSurname.text.toString()
        val usEmail = etUsEmail.text.toString()
        val usPassword = etUsPassword.text.toString()


        val usId =   databaseRf.push().key!!

        val user = UserModel(usId, usName, usSurname, usEmail, usPassword)

        databaseRf.child(usId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}

