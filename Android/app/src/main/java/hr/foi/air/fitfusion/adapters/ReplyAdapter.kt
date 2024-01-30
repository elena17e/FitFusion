package hr.foi.air.fitfusion.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.entities.Reply
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReplyAdapter(private val repliesList : List<Reply>) : RecyclerView.Adapter<ReplyAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val replyContent: TextView = view.findViewById(R.id.tv_reply_content )
        private val replyDate: TextView = view.findViewById(R.id.tv_reply_date)
        private val replyAuthor: TextView = view.findViewById(R.id.tv_reply_author)

        @SuppressLint("SetTextI18n")
        fun bind(reply: Reply) {
            replyContent.text = reply.content
            replyAuthor.text = reply.authorFirstName + reply.authorLastName
            replyDate.text = convertTimestampToDateString(reply.timestamp)
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
            .inflate(R.layout.reply_list_item, parent, false)

        return TaskViewHolder(taskView)
    }

    override fun getItemCount() = repliesList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(repliesList[position])

    }
}