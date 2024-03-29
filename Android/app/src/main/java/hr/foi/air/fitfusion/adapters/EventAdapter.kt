package hr.foi.air.fitfusion.adapters

import hr.foi.air.fitfusion.R
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import hr.foi.air.fitfusion.TrainingDetails
import hr.foi.air.fitfusion.adapters.CalendarUtils.formattedTime
import hr.foi.air.fitfusion.entities.Event



class EventAdapter(context: Context, events: List<Event>) : ArrayAdapter<Event>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_cell, parent, false)
        }

        val eventCellTV = view!!.findViewById<TextView>(R.id.eventCellTV)
        val eventTitle = "${event!!.name} ${formattedTime(event.time)}"
        eventCellTV.text = eventTitle

        val stringDate = event.date.toString()
        val stringTime = event.time.toString()
        val stringTimeEnd = event.timeEnd.toString()
        view.setOnClickListener {
            val intent = Intent(context, TrainingDetails::class.java).apply {
                putExtra("eventType", event.name)
                putExtra("eventDate", stringDate)
                putExtra("eventTime", stringTime)
                putExtra("eventTimeEnd", stringTimeEnd)
                putExtra("eventParticipants", event.participants)
                putExtra("eventId", event.id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        return view
    }
}