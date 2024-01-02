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
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.entities.Post


class ForumFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreatePost: FloatingActionButton


    private lateinit var taskAdapter: TaskAdapter

    private val firebaseManager = FirebaseManager()


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

        postButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val loggedInUser = LoggedInUser(requireContext())
                val authorFirstName = loggedInUser.getFirstName() ?: ""
                val authorLastName = loggedInUser.getLastName() ?: ""
                val timestamp = System.currentTimeMillis()
                firebaseManager.savePost(title, content, timestamp, authorFirstName, authorLastName)
                dialog.dismiss()
            }
        }
    }

    private fun fetchPostsFromFirebase() {
        firebaseManager.fetchPosts { posts ->
            taskAdapter = TaskAdapter(posts)
            recyclerView.adapter = taskAdapter

        }
    }
}
