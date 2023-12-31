package hr.foi.air.fitfusion

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReplyActivity : AppCompatActivity(){
    private lateinit var btnPostReply: Button
    private val database = FirebaseDatabase.getInstance()
    private val postRef = database.getReference("Replies")
    private lateinit var loggedInUser: LoggedInUser
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

        btnPostReply = findViewById(R.id.buttonReply)
        btnPostReply.setOnClickListener {
            showDialog()
        }
    }

    private fun convertTimestampToDateString(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return sdf.format(calendar.time)
    }

    private fun showDialog() {
        val newTaskDialogView = LayoutInflater
            .from(this)
            .inflate(R.layout.new_forum_reply, null)

        val contentEditText = newTaskDialogView.findViewById<EditText>(R.id.contentEditText)
        val postButton = newTaskDialogView.findViewById<Button>(R.id.postButton)

        val dialog = AlertDialog.Builder(this)
            .setView(newTaskDialogView)
            .show()

        postButton.setOnClickListener{
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()){
                loggedInUser = LoggedInUser(this)
                val authorFirstName = loggedInUser.getFirstName()
                val authorLastName = loggedInUser.getLastName()
                val timestamp = System.currentTimeMillis()
                savePostToFirebase(content, timestamp, authorFirstName, authorLastName)
                dialog.dismiss()
            }
        }
    }
    private fun savePostToFirebase(content: String, timestamp: Long, authorFirstName: String?, authorLastName: String?){
        val postId = postRef.push().key
        val newPost =
            mapOf(
                "id" to postId,
                "content" to content,
                "timestamp" to timestamp,
                "authorFirstName" to authorFirstName,
                "authorLastName" to authorLastName
            )

        if (postId != null && newPost != null){
            postRef.child(postId).setValue(newPost)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
    }

}