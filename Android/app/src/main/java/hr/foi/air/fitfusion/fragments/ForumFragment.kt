package hr.foi.air.fitfusion.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.ReplyActivity
import hr.foi.air.fitfusion.TrainerActivity
import hr.foi.air.fitfusion.WelcomeAdminActivity
import hr.foi.air.fitfusion.adapters.TaskAdapter
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.entities.Post


class ForumFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreatePost: FloatingActionButton

    //private lateinit var btnReplyPost: Button

    private lateinit var taskAdapter: TaskAdapter

    private val database = FirebaseDatabase.getInstance()
    private val postRef = database.getReference("Posts")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_forum, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.list_posts)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        taskAdapter = TaskAdapter(emptyList())
        recyclerView.adapter = taskAdapter

        btnCreatePost = view.findViewById(R.id.createNewPost)
        btnCreatePost.setOnClickListener {
            showDialog()
        }

        fetchPostsFromFirebase()


        /*btnReplyPost = view.findViewById(R.id.btn_clickable)
        btnReplyPost.setOnClickListener{

        }*/
    }
    private fun showDialog() {
        val newTaskDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_forum_post, null)

        val titleEditText = newTaskDialogView.findViewById<EditText>(R.id.titleEditText)
        val contentEditText = newTaskDialogView.findViewById<EditText>(R.id.contentEditText)
        val postButton = newTaskDialogView.findViewById<Button>(R.id.postButton)

        val dialog = AlertDialog.Builder(context)
            .setView(newTaskDialogView)
            .show()

        postButton.setOnClickListener{
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()){
                val loggedInUser = LoggedInUser(requireContext())
                val authorFirstName = loggedInUser.getFirstName()?: ""
                val authorLastName = loggedInUser.getLastName()?: ""
                val timestamp = System.currentTimeMillis()
                savePostToFirebase(title, content, timestamp, authorFirstName, authorLastName)
                dialog.dismiss()
            }
        }
    }
    private fun savePostToFirebase(title: String, content: String, timestamp: Long, authorFirstName: String, authorLastName: String){
        val postId = postRef.push().key
        val newPost =
            mapOf(
                "id" to postId,
                "title" to title,
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
    private fun fetchPostsFromFirebase() {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts: MutableList<Post> = mutableListOf()

                for (postSnapshot in dataSnapshot.children) {
                    val id = postSnapshot.child("id").getValue(String::class.java) ?: ""
                    val title = postSnapshot.child("title").getValue(String::class.java) ?: ""
                    val content = postSnapshot.child("content").getValue(String::class.java) ?: ""
                    val timestamp = postSnapshot.child("timestamp").getValue(Long::class.java) ?: 0
                    val authorFirstName = postSnapshot.child("authorFirstName").getValue(String::class.java) ?: ""
                    val authorLastName = postSnapshot.child("authorLastName").getValue(String::class.java) ?: ""


                    val post = Post(id, title, content, timestamp, authorFirstName, authorLastName)
                    posts.add(post)
                }

                taskAdapter = TaskAdapter(posts)
                recyclerView.adapter = taskAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("ForumFragment", "onCancelled: ${databaseError.message}")
            }
        })
    }
}
