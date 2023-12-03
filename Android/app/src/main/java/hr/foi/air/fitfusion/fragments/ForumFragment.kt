package hr.foi.air.fitfusion.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.adapters.TaskAdapter
import hr.foi.air.fitfusion.entities.Post

class ForumFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreatePost: FloatingActionButton

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
        recyclerView = view.findViewById(R.id.list_posts)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        taskAdapter = TaskAdapter(emptyList())
        recyclerView.adapter = taskAdapter

        btnCreatePost = view.findViewById(R.id.createNewPost)
        btnCreatePost.setOnClickListener {
            showDialog()
        }

        fetchPostsFromFirebase()
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

                val timestamp = System.currentTimeMillis()
                savePostToFirebase(title, content, timestamp )
                dialog.dismiss()
            }
        }
    }
    private fun savePostToFirebase(title: String, content: String, timestamp: Long){
        val postId = postRef.push().key
        val newPost = postId?.let {
            mapOf(
                "title" to title,
                "content" to content,
                //"author" to author, //autor nije dovršen
                "timestamp" to timestamp
            )
        }
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
                    val title = postSnapshot.child("title").getValue(String::class.java) ?: ""
                    val content = postSnapshot.child("content").getValue(String::class.java) ?: ""
                    val timestamp = postSnapshot.child("timestamp").getValue(Long::class.java) ?: 0

                    val post = Post(title, content, timestamp)
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
