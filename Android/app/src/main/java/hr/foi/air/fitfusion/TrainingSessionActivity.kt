package hr.foi.air.fitfusion

import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import java.util.Calendar
import java.util.Locale

class TrainingSessionActivity : AppCompatActivity() {

    private lateinit var etTime: EditText
    private lateinit var etTimeEnd: EditText
    private lateinit var etDate: EditText
    private lateinit var etNumber: EditText
    private lateinit var sspinner: Spinner
    private lateinit var btnDone: Button
    private lateinit var btnCancel: Button
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var loggedInUser: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_training_session)
        loggedInUser = LoggedInUser(this)
        etTime = findViewById(R.id.Time)
        etTimeEnd = findViewById(R.id.TimeEnd)
        etDate = findViewById(R.id.Date)
        etNumber = findViewById(R.id.Number)
        sspinner = findViewById(R.id.spinner)
        btnDone = findViewById(R.id.Done)
        btnCancel = findViewById(R.id.Cancel)

        firebaseManager = FirebaseManager()

        val classNames = listOf(
            "Yoga",
            "Strength",
            "Cardio"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sspinner.adapter = adapter

        btnDone.setOnClickListener {
            saveTrainingSession()

            val notificationId = (System.currentTimeMillis()%1000).toInt()

            val notificationManager = ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager

            val notificationBuilder = NotificationCompat.Builder(this, "trainingSignUp")
                .setSmallIcon(R.drawable.baseline_assignment_turned_in_24)
                .setContentTitle("Training session")
                .setContentText("You have successfully added your training to the calendar!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(notificationId, notificationBuilder.build())
        }

        val btnSelectTime: Button = findViewById(R.id.SelectTime)
        val btnSelectTimeEnd: Button = findViewById(R.id.SelectTimeEnd)
        val btnSelectDate: Button = findViewById(R.id.SelectDate)

        btnSelectTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnSelectTimeEnd.setOnClickListener {
            showTimeEndPickerDialog()
        }

        btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, WelcomeTrainerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveTrainingSession() {
        val time = etTime.text.toString()
        val timeEnd = etTimeEnd.text.toString()
        val date = etDate.text.toString()
        val participants = etNumber.text.toString()
        val type = sspinner.selectedItem.toString()
        val trainerId = loggedInUser.getUserId()

        firebaseManager.saveTrainingSession(time,timeEnd, date, participants, type, trainerId) { success ->
            if (success) {
                Toast.makeText(this, "Training session added successfully", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, WelcomeTrainerActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to add training session", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val selectedTime =
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        selectedHour,
                        selectedMinute
                    )
                etTime.setText(selectedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showTimeEndPickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val selectedTime =
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        selectedHour,
                        selectedMinute
                    )
                etTimeEnd.setText(selectedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format(
                    Locale.getDefault(),
                    "%02d.%02d.%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                etDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
