package hr.foi.air.fitfusion

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.adapters.ReplyAdapter
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReplyActivity : AppCompatActivity(){
    private lateinit var btnPostReply: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var idPost: String
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var loggedInUser: LoggedInUser
    private lateinit var replyAdapter: ReplyAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_post)

        recyclerView = findViewById(R.id.list_replies)
        recyclerView.layoutManager = LinearLayoutManager(this)

        replyAdapter = ReplyAdapter(emptyList())
        recyclerView.adapter = replyAdapter

        firebaseManager = FirebaseManager()
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val contentTextView = findViewById<TextView>(R.id.contentTextView)

        val postTitle = intent.getStringExtra("postTitle")?: ""
        val postContent = intent.getStringExtra("postContent")
        val postAuthor = intent.getStringExtra("postAuthor")
        val postTimestamp = intent.getLongExtra("postTimestamp", 0)
        firebaseManager.getPostId(postTitle){ postId ->
            idPost = postId
        }

        titleTextView.text = postTitle
        authorTextView.text = postAuthor
        contentTextView.text = postContent

        val formattedDate = convertTimestampToDateString(postTimestamp)
        authorTextView.text = "$postAuthor - $formattedDate"

        btnPostReply = findViewById(R.id.buttonReply)
        btnPostReply.setOnClickListener {
            showDialog()
        }
        firebaseManager.fetchReplyFromFirebase(postTitle, recyclerView)
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
                firebaseManager.saveReplyToFirebase(content, timestamp, authorFirstName, authorLastName, idPost)
                dialog.dismiss()
            }
        }
    }
}