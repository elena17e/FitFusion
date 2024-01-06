package hr.foi.air.fitfusion.views
import hr.foi.air.fitfusion.R
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.adapters.CalendarAdapter
import java.time.LocalDate

class CalendarViewHolder(
    itemView: View,
    private var onItemListener: CalendarAdapter.OnItemListener,
    days: ArrayList<LocalDate>
) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    private val days: ArrayList<LocalDate>
    var dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
    var parentView : View = itemView.findViewById(R.id.parentView)

    init {
        itemView.setOnClickListener(this)
        this.days = days
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}