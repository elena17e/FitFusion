package hr.foi.air.fitfusion.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.ReplyActivity
import hr.foi.air.fitfusion.entities.Post
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskAdapter(private val postsList : List<Post>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

        private lateinit var btnReplyPost: Button

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val taskTitle: TextView = view.findViewById(R.id.tv_post_title )
        private val taskDate: TextView = view.findViewById(R.id.tv_post_date)
        private val taskAuthor: TextView = view.findViewById(R.id.tv_post_author)


       init {
            view.setOnClickListener {
                val post = postsList[adapterPosition]
                val intent = Intent(view.context, ReplyActivity::class.java).apply {
                    putExtra("postTitle", post.title)
                    putExtra("postContent", post.content)
                    putExtra("postAuthor", "${post.authorFirstName} ${post.authorLastName}")
                    putExtra("postTimestamp", post.timestamp)
                }
                view.context.startActivity(intent)
            }
        }
        fun bind(post: Post) {
            taskTitle.text = post.title
            taskAuthor.text = "${post.authorFirstName} ${post.authorLastName}"
            //taskContent.text = post.content
            taskDate.text = convertTimestampToDateString(post.timestamp)
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