package hr.foi.air.fitfusion

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReplyActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_post)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val contentTextView = findViewById<TextView>(R.id.contentTextView)

        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        val postAuthor = intent.getStringExtra("postAuthor")
        val postTimestamp = intent.getLongExtra("postTimestamp", 0)

        titleTextView.text = postTitle
        authorTextView.text = postAuthor
        contentTextView.text = postContent

        val formattedDate = convertTimestampToDateString(postTimestamp)
        authorTextView.text = "$postAuthor - $formattedDate"
    }

    private fun convertTimestampToDateString(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return sdf.format(calendar.time)
    }

}