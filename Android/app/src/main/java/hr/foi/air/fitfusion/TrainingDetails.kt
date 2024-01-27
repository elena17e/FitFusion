package hr.foi.air.fitfusion

import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TrainingDetails : AppCompatActivity() {
    private lateinit var textViewDisplayId: TextView
    private lateinit var textViewDisplayDate: TextView
    private lateinit var textViewDisplayTime: TextView
    private lateinit var textViewDisplayTimeEnd: TextView
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
        textViewDisplayTimeEnd = findViewById(R.id.textViewDisplayTimeEnd)
        textViewDisplayType = findViewById(R.id.textViewDisplayType)
        textViewDisplayParticipants = findViewById(R.id.textViewDisplayParticipants)
        cancelButton = findViewById(R.id.Cancel)
        applyButton = findViewById(R.id.Apply)

        val id = intent.getStringExtra("eventId")
        val date = intent.getStringExtra("eventDate")
        val time = intent.getStringExtra("eventTime")
        val timeEnd = intent.getStringExtra("eventTimeEnd")
        val type = intent.getStringExtra("eventType")
        val participants = intent.getStringExtra("eventParticipants")


        textViewDisplayId.text = id
        textViewDisplayDate.text = date
        textViewDisplayTime.text = time
        textViewDisplayTimeEnd.text = timeEnd
        textViewDisplayType.text = type
        textViewDisplayParticipants.text = participants


        cancelButton.setOnClickListener {
            val intent = Intent(this, WeekViewActivity::class.java)
            startActivity(intent)
        }
        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateToString = LocalDate.parse(date, formatter)

        if (loggedUsertype == "trainer" || participants == "0" || dateToString.isBefore(current)) {
            applyButton.visibility = View.GONE
        }
        firebaseManager.checkIfUserIsAlreadyApplied(this, id) { hasApplied ->
            if (hasApplied) {
                Toast.makeText(this, "You have already applied for this training!", Toast.LENGTH_SHORT).show()
                applyButton.visibility = View.GONE
            } else {
                if (loggedUsertype == "user" && participants != "0" && (dateToString.equals(current) || dateToString.isAfter(current))) {
                    applyButton.setOnClickListener {
                        firebaseManager.applyForTraining(this, id)
                        val intent = Intent(this, WeekViewActivity::class.java)
                        startActivity(intent)

                        val notificationId = (System.currentTimeMillis() % 10000).toInt()

                        val notificationManager = ContextCompat.getSystemService(
                            this,
                            NotificationManager::class.java
                        ) as NotificationManager

                        val notificationBuilder = NotificationCompat.Builder(this, "trainingSignUp")
                            .setSmallIcon(R.drawable.baseline_assignment_turned_in_24)
                            .setContentTitle("Training session")
                            .setContentText("You have successfully applied for training!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        notificationManager.notify(notificationId, notificationBuilder.build())
                    }
                }

            }
        }

    }
}