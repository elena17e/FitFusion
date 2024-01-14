package hr.foi.air.fitfusion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser

class TrainingDetails : AppCompatActivity() {
    private lateinit var textViewDisplayId: TextView
    private lateinit var textViewDisplayDate: TextView
    private lateinit var textViewDisplayTime: TextView
    private lateinit var textViewDisplayType: TextView
    private lateinit var textViewDisplayParticipants: TextView
    private lateinit var cancelButton: Button
    private lateinit var applyButton: Button
    private val firebaseManager = FirebaseManager()
    private lateinit var loggedInUser: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_details)
        loggedInUser = LoggedInUser(this)
        val loggedUsertype = loggedInUser.getType()
        textViewDisplayId = findViewById(R.id.textViewDisplayId)
        textViewDisplayDate = findViewById(R.id.textViewDisplayDate)
        textViewDisplayTime = findViewById(R.id.textViewDisplayTime)
        textViewDisplayType = findViewById(R.id.textViewDisplayType)
        textViewDisplayParticipants = findViewById(R.id.textViewDisplayParticipants)
        cancelButton = findViewById(R.id.Cancel)
        applyButton = findViewById(R.id.Apply)

        val id = intent.getStringExtra("eventId")
        val date = intent.getStringExtra("eventDate")
        val time = intent.getStringExtra("eventTime")
        val type = intent.getStringExtra("eventType")
        val participants = intent.getStringExtra("eventParticipants")


        textViewDisplayId.text = id
        textViewDisplayDate.text = date
        textViewDisplayTime.text = time
        textViewDisplayType.text = type
        textViewDisplayParticipants.text = participants


        cancelButton.setOnClickListener {
            val intent = Intent(this, WeekViewActivity::class.java)
            startActivity(intent)
        }

        if (loggedUsertype == "trainer" || participants == "0") {
            applyButton.visibility = View.GONE
        }
        if (loggedUsertype == "user" && participants != "0") {
            applyButton.setOnClickListener {
                firebaseManager.applyForTraining(this, id)
                val intent = Intent(this, WeekViewActivity::class.java)
                startActivity(intent)
            }
        }
    }
}