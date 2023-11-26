package hr.foi.air.fitfusion.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.adapters.TaskAdapter
import hr.foi.air.fitfusion.helpers.MockDataLoader

class ForumFragment : Fragment() {
    private val mockTasks = MockDataLoader.getDemoData()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreateTask: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mockTasks.forEach { Log.i("MOCK_PENDING_TASKS", it.name) }
        return inflater.inflate(R.layout.fragment_forum, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.list_posts)
        recyclerView.adapter = TaskAdapter(MockDataLoader.getDemoData())
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        btnCreateTask = view.findViewById(R.id.createNewPost)
        btnCreateTask.setOnClickListener {
            showDialog()
        }
    }
    private fun showDialog() {
        val newTaskDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_forum_post, null)
        AlertDialog.Builder(context)
            .setView(newTaskDialogView)
            //.setTitle(getString(R.string.create_a_new_task))
            .show()
    }
}
