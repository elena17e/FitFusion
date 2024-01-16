package hr.foi.air.fitfusion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.adapters.CalendarAdapter
import hr.foi.air.fitfusion.adapters.CalendarUtils
import hr.foi.air.fitfusion.adapters.EventAdapter
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.LoggedInUser
import hr.foi.air.fitfusion.entities.Event
import java.time.LocalDate


class WeekViewActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var eventListView: ListView? = null
    private val firebaseManager = FirebaseManager()
    private lateinit var loggedInUser: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_view_calendar)
        initWidgets()
        setWeekView()
        loggedInUser = LoggedInUser(this)
        val type = loggedInUser.getType()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (type == "user") {
                    val intent = Intent(this@WeekViewActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                if (type == "trainer") {
                    val intent = Intent(this@WeekViewActivity, WelcomeTrainerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }

                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)


        val backButton = findViewById<ImageButton>(R.id.imageButtonMonthlyCal)
        backButton.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }
        firebaseManager.fetchTrainingFromFirebase()
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
        eventListView = findViewById(R.id.eventListView)
    }

    private fun setWeekView() {
        Log.d("WeekViewActivity", "Accessing selectedDate")
        monthYearText!!.text = CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate)
        val days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(days, this)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        setEventAdapter()
    }

    @Suppress("UNUSED_PARAMETER")
    fun previousWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate!!.minusWeeks(1)
        setWeekView()
    }

    @Suppress("UNUSED_PARAMETER")
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
        setEventAdapter()
    }

    private fun setEventAdapter() {

        val dailyEvents: ArrayList<Event> = Event.eventsForDate(CalendarUtils.selectedDate)
        val eventAdapter = EventAdapter(applicationContext, dailyEvents)
        eventListView?.adapter = eventAdapter
    }

}