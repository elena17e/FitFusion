package hr.foi.air.fitfusion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.entities.Post
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskAdapter(private val postsList : List<Post>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val taskTitle: TextView
        private val taskContet: TextView
        private val taskDate: TextView

        init {
            taskTitle = view.findViewById(R.id.tv_post_title)
            taskContet = view.findViewById(R.id.tv_post_author) //ovo je za sadržaj
            taskDate = view.findViewById(R.id.tv_post_date)
        }

        fun bind(post: Post) {
            taskTitle.text = post.title
            taskContet.text = post.content
            val timestamp = post.timestamp
            val formattedDate = convertTimestampToDateString(timestamp)
            taskDate.text = formattedDate
        }
        private fun convertTimestampToDateString(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            return sdf.format(calendar.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(taskView)
    }

    override fun getItemCount() = postsList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(postsList[position])
    }
}