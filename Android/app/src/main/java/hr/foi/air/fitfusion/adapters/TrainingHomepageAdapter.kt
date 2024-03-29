package hr.foi.air.fitfusion.adapters

import android.app.NotificationManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.data_classes.TrainingModel


@Suppress("UNUSED_PARAMETER")
class TrainingHomepageAdapter(private val trainingsList: ArrayList<TrainingModel>, private val onCalendarClick: () -> Unit, private val onCancelClick: (TrainingModel) -> Unit) : RecyclerView.Adapter<TrainingHomepageAdapter.TrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_booked_class, parent, false)
        return TrainingViewHolder(itemView)
    }

    override fun getItemCount(): Int = trainingsList.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainingsList[position]
        holder.bind(training, onCalendarClick)
        holder.itemView.findViewById<View>(R.id.cancelTraining).setOnClickListener {
            onCancelClick(training)

            val notificationId = (System.currentTimeMillis()%1000).toInt()

            val context = holder.itemView.context
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            val notificationBuilder = NotificationCompat.Builder(context, "trainingSignUp")
                .setSmallIcon(R.drawable.baseline_assignment_turned_in_24)
                .setContentTitle("Training session")
                .setContentText("You have successfully canceled your training!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val type: TextView = itemView.findViewById(R.id.type)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val time: TextView = itemView.findViewById(R.id.time)



        fun bind(training: TrainingModel, onCalendarClick: () -> Unit) {
            type.text = training.type
            date.text = training.date
            time.text = training.time

        }

    }
}
