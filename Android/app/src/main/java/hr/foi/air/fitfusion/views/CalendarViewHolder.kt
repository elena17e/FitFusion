package hr.foi.air.fitfusion.views
import hr.foi.air.fitfusion.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.adapters.CalendarAdapter

public class CalendarViewHolder (
    itemView: View,
    private val onItemListener: CalendarAdapter.OnItemListener,
) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    val parentView: View? = null

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text.toString())
    }
}