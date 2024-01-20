package hr.foi.air.fitfusion

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import java.util.Calendar
import java.util.Locale


class TrainingSessionDetailsActivity : AppCompatActivity() {
    private lateinit var sspinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var timeEndEditText: EditText
    private lateinit var participantsEditText: EditText
    private lateinit var switchCanceled: SwitchCompat
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.training_session)

        sspinner = findViewById(R.id.spinner)
        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        timeEndEditText = findViewById(R.id.timeEndEditText)
        participantsEditText = findViewById(R.id.participantsEditText)
        switchCanceled = findViewById(R.id.switch1)
        editButton = findViewById(R.id.Edit)
        cancelButton = findViewById(R.id.Cancel)
        val classNames = listOf(
            "Yoga",
            "Strength",
            "Cardio"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sspinner.adapter = adapter

        val type = intent.getStringExtra("type")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val timeEnd = intent.getStringExtra("time_end")
        val participants = intent.getStringExtra("participants")

        sspinner.setSelection(classNames.indexOf(type as String))
        dateEditText.setText(date)
        timeEditText.setText(time)
        timeEndEditText.setText(timeEnd)
        participantsEditText.setText(participants)
        switchCanceled.isChecked = intent.getStringExtra("state").equals("active")



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


        editButton.setOnClickListener {
            var type: String = sspinner.selectedItem.toString()
            var date: String = dateEditText.text.toString()
            var time: String = timeEditText.text.toString()
            var timeEnd:String = timeEndEditText.text.toString()
            var participants: String = participantsEditText.text.toString()
            var state: String = if (switchCanceled.isChecked) "active" else "inactive"
            var firebaseManager: FirebaseManager = FirebaseManager()
            firebaseManager.updateTrainingSession(
                date,
                participants,
                time,
                timeEnd,
                type,
                state,
                intent.getStringExtra("sessionId").toString(),
                intent.getStringExtra("userId").toString()
            )

            val intent = Intent(this, WelcomeTrainerActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Training session saved successfully", Toast.LENGTH_SHORT)
                .show()
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, WelcomeTrainerActivity::class.java)
            startActivity(intent)
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
                    timeEditText.setText(selectedTime)
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
                timeEndEditText.setText(selectedTime)
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
                    dateEditText.setText(selectedDate)
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }
}
