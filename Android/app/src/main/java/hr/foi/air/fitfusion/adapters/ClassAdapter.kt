package hr.foi.air.fitfusion.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.entities.ClassesStrength


class ClassAdapter (
    private val classList : ArrayList<ClassesStrength>,
    private val listener: RecyclerViewEvent
) : RecyclerView.Adapter<ClassAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.class_list_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = classList[position]

     holder.date.text = currentitem.date
     holder.time.text = currentitem.time
     holder.type.text = currentitem.type
     holder.participants.text = currentitem.participants
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val date : TextView = itemView.findViewById(R.id.tvDate)
        val time : TextView = itemView.findViewById(R.id.tvTime)
        val type : TextView = itemView.findViewById(R.id.tvType)
        val participants : TextView = itemView.findViewById(R.id.tvParticipants)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface RecyclerViewEvent {
        fun onItemClick(position: Int)
    }
}