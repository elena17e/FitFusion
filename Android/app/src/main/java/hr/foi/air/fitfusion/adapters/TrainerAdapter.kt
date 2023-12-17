package hr.foi.air.fitfusion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.entities.Trainer

class TrainerAdapter(private val trainerList: ArrayList<Trainer>): RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder>() {

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
        holder.bind(trainerList[position])
    }
    class TrainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val firstName: TextView = itemView.findViewById(R.id.trainerName)
        private val lastName: TextView = itemView.findViewById(R.id.trainerLastName)
        private val email: TextView = itemView.findViewById(R.id.trainerEmail)
        fun bind(trainer: Trainer) {
            firstName.text = trainer.FirstName
            lastName.text = trainer.LastName
            email.text = trainer.Email
        }
    }
}