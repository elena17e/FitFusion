package hr.foi.air.fitfusion

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.adapters.CalendarAdapter2
import hr.foi.air.fitfusion.adapters.CalendarAdapter2.OnItemListener
import hr.foi.air.fitfusion.adapters.CalendarUtils
import hr.foi.air.fitfusion.adapters.CalendarUtils.daysInWeekArray
import hr.foi.air.fitfusion.adapters.CalendarUtils.monthYearFromDate
import java.time.LocalDate


class WeekViewActivity : AppCompatActivity(), OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var eventListView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_view_calendar)
        initWidgets()
        setWeekView()
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
        //eventListView = findViewById<ListView>(R.id.eventListView)
    }

    private fun setWeekView() {
        monthYearText!!.text = monthYearFromDate(CalendarUtils.selectedDate)
        val days : ArrayList<LocalDate?> = daysInWeekArray(CalendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter2(days, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        //setEventAdpater()
    }

    fun previousWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.plusWeeks(1)
        setWeekView()
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        CalendarUtils.selectedDate = date
        setWeekView()
    }

    override fun onResume() {
        super.onResume()
        //setEventAdpater()
    }

    /*private fun setEventAdpater() {
        val dailyEvents: ArrayList<Event> = Event.eventsForDate(CalendarUtils.selectedDate)
        val eventAdapter = EventAdapter(applicationContext, dailyEvents)
        eventListView!!.adapter = eventAdapter
    }*/

    /*fun newEventAction(view: View?) {
        startActivity(Intent(this, EventEditActivity::class.java))
    }*/

    fun backToMonthView(view: View) {}
}