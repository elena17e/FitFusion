package hr.foi.air.fitfusion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.data_classes.UserModel

class TrainerAdapter(private val trainerList: ArrayList<UserModel>, private val onDeleteClick: (UserModel) -> Unit): RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainerViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.trainer_card, parent, false)
        return TrainerViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return trainerList.size
    }
    override fun onBindViewHolder(holder: TrainerViewHolder, position: Int) {
        val trainer = trainerList[position]
        holder.bind(trainer)
        holder.itemView.findViewById<View>(R.id.deleteTrainer).setOnClickListener{
            onDeleteClick(trainer)
        }
    }
    class TrainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val firstName: TextView = itemView.findViewById(R.id.trainerName)
        private val lastName: TextView = itemView.findViewById(R.id.trainerLastName)
        private val email: TextView = itemView.findViewById(R.id.trainerEmail)
        fun bind(trainer: UserModel) {
            firstName.text = trainer.firstName
            lastName.text = trainer.lastName
            email.text = trainer.email
        }
    }
}