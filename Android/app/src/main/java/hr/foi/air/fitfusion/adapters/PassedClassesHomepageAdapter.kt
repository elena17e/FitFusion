package hr.foi.air.fitfusion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.data_classes.TrainingModel

@Suppress("UNUSED_PARAMETER")
class PassedClassesHomepageAdapter (private val trainingsList: ArrayList<TrainingModel>, private val onCalendarClick: () -> Unit) : RecyclerView.Adapter<PassedClassesHomepageAdapter.TrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_passed_class, parent, false)
        return TrainingViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: TrainingViewHolder,
        position: Int
    ) {
        val training = trainingsList[position]
        holder.bind(training, onCalendarClick)
    }

    override fun getItemCount(): Int = trainingsList.size


    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val type: TextView = itemView.findViewById(R.id.type_passed)
        private val date: TextView = itemView.findViewById(R.id.date_passed)
        private val time: TextView = itemView.findViewById(R.id.time_passed)



        fun bind(training: TrainingModel, onCalendarClick: () -> Unit) {
            type.text = training.type
            date.text = training.date
            time.text = training.time

        }
    }
}