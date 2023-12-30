package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat


class TrainingSessionDetailsActivity : AppCompatActivity() {
    private lateinit var typeEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var participantsEditText: EditText
    private lateinit var switchCanceled: SwitchCompat
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.training_session)

        typeEditText = findViewById(R.id.typeEditText)
        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)
        participantsEditText = findViewById(R.id.participantsEditText)
        switchCanceled = findViewById(R.id.switch1)
        editButton = findViewById(R.id.Edit)
        cancelButton = findViewById(R.id.Cancel)


        val type = intent.getStringExtra("type")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val participants = intent.getStringExtra("participants")

        typeEditText.setText(type)
        dateEditText.setText(date)
        timeEditText.setText(time)
        participantsEditText.setText(participants)

        switchCanceled.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){

            }else{

            }
        }

        editButton.setOnClickListener {
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, WelcomeTrainerActivity::class.java)
            startActivity(intent)
        }

    }
}
