package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import hr.foi.air.fitfusion.data_classes.TrainingModel

class TrainingSessionActivity : AppCompatActivity() {
    private lateinit var etTime: EditText
    private lateinit var etDate: EditText
    private lateinit var etNumber: EditText
    private lateinit var sspinner: Spinner
    private lateinit var btnDone: Button
    private lateinit var btnCancel: Button
    private lateinit var databaseRf: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_training_session)

        etTime = findViewById(R.id.Time)
        etDate = findViewById(R.id.Date)
        etNumber = findViewById(R.id.Number)
        sspinner = findViewById(R.id.spinner)
        btnDone = findViewById(R.id.Done)
        btnCancel = findViewById(R.id.Cancel)

        val classNames = listOf(
            "Yoga",
            "Strength",
            "Cardio"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sspinner.adapter = adapter

        databaseRf = FirebaseDatabase.getInstance().getReference("Training")


        btnDone.setOnClickListener {
            val time = etTime.text.toString()
            val date = etDate.text.toString()
            val participants = etNumber.text.toString()
            val type = sspinner.selectedItem.toString()

            val timeRegex = Regex("^\\d{2}:\\d{2}\$")
            val dateRegex = Regex("^\\d{2}\\.\\d{2}\\.\\d{4}\$")

            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId: String = currentUser?.uid ?: ""

            if (type.isNotEmpty() && participants.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                if (!time.matches(timeRegex) || !date.matches(dateRegex)) {
                    Toast.makeText(this, "Invalid time or date format", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                        sdf.parse(time)

                        val dateSdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        dateSdf.isLenient = false
                        dateSdf.parse(date)

                        val sessionId: String = databaseRf.push().key ?: ""

                        val trainingSession = TrainingModel(
                            id = sessionId,
                            date = date,
                            participants = participants,
                            state = "active",
                            time = time,
                            userId = userId,
                            type = type
                        )

                        databaseRf.child(sessionId).setValue(trainingSession)

                        Toast.makeText(
                            this,
                            "Training session saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, WelcomeTrainerActivity::class.java)
                        startActivity(intent)
                    } catch (e: ParseException) {
                        Toast.makeText(this, "Invalid time or date", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, WelcomeTrainerActivity::class.java)
            startActivity(intent)
        }

    }
}