package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import java.util.regex.Pattern
import hr.foi.air.fitfusion.data_classes.FirebaseManager

class TrainerActivity : AppCompatActivity() {

    private lateinit var etfirstname: EditText
    private lateinit var etlastname: EditText
    private lateinit var etemail: EditText
    private lateinit var etpassword: EditText
    private lateinit var etdescription: EditText
    private lateinit var btnDone: Button
    private lateinit var btnCancel: Button
    private lateinit var firebaseManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_trainer)

        etfirstname = findViewById(R.id.etfirstname)
        etlastname = findViewById(R.id.etlastname)
        etemail = findViewById(R.id.etemail)
        etpassword = findViewById(R.id.etpassword)
        etdescription = findViewById(R.id.etdescription)
        btnDone = findViewById(R.id.Done)
        btnCancel = findViewById(R.id.Cancel)

        firebaseManager = FirebaseManager()

        btnDone.setOnClickListener {
            val firstName = etfirstname.text.toString().trim()
            val lastName = etlastname.text.toString().trim()
            val email = etemail.text.toString().trim()
            val password = etpassword.text.toString().trim()
            val description = etdescription.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(
                    this,
                    "Password must be at least 5 characters long and include a combination of uppercase and lowercase letters, numbers, and symbols",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            firebaseManager.addTrainer(firstName, lastName, email, password, description) { isSuccess, message ->
                if (isSuccess) {
                    Toast.makeText(this@TrainerActivity, message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TrainerActivity, WelcomeAdminActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@TrainerActivity, message ?: "Error adding trainer", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, WelcomeAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!.+@\$#%^&*()])[A-Za-z\\d!.+@\$#%^&*()]{5,}\$")
        return passwordPattern.matcher(password).matches()
    }
}