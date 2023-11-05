package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import hr.foi.air.fitfusion.ui.theme.FitFusionTheme

class SignUpActivity : ComponentActivity() {
    private lateinit var etUsName: EditText
    private lateinit var etUsSurname: EditText
    private lateinit var etUsEmail: EditText
    private lateinit var etUsPassword: EditText
    private lateinit var btnDone: Button
    private lateinit var btnReturn: Button


    private lateinit var databaseRf: DatabaseReference
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
            saveUserData()
        }
        btnReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



data class UserModel(
    var usId: String?= null,
    var First_Name: String?=null,
    var Last_Name: String?=null,
    var Email: String?=null,
    var Password: String?=null
)

private fun saveUserData() {

    val First_Name = etUsName.text.toString()
    val Last_Name = etUsSurname.text.toString()
    val Email = etUsEmail.text.toString()
    val Password = etUsPassword.text.toString()


    val usId =   databaseRf.push().key!!

    val User = UserModel(usId, First_Name, Last_Name, Email, Password)

    databaseRf.child(usId).setValue(User)
        .addOnCompleteListener {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

        }.addOnFailureListener { err ->
            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
        }

}
}
