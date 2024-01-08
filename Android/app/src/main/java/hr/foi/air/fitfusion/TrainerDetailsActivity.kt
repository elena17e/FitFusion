package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TrainerDetailsActivity : AppCompatActivity() {
    private lateinit var textViewDisplayFirstName:  TextView
    private lateinit var textViewDisplayLastName: TextView
    private lateinit var textViewDisplayEmail: TextView
    private lateinit var textViewDisplayDescription: TextView
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trainer_details)

        textViewDisplayFirstName = findViewById(R.id.textViewDisplayFirstName)
        textViewDisplayLastName = findViewById(R.id.textViewDisplayLastName)
        textViewDisplayEmail = findViewById(R.id.textViewDisplayEmail)
        textViewDisplayDescription = findViewById(R.id.textViewDisplayDescription)
        cancelButton = findViewById(R.id.Cancel)


        val FirstName = intent.getStringExtra("firstName")
        val LastName = intent.getStringExtra("lastName")
        val Email = intent.getStringExtra("email")
        val Description = intent.getStringExtra("description")


        textViewDisplayFirstName.text = FirstName
        textViewDisplayLastName.text = LastName
        textViewDisplayEmail.text = Email
        textViewDisplayDescription.text = Description


        cancelButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }
}
