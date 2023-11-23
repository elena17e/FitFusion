package hr.foi.air.fitfusion.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.helpers.MockDataLoader

class ForumFragment : Fragment() {
    private val mockTasks = MockDataLoader.getDemoData()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mockTasks.forEach { Log.i("MOCK_PENDING_TASKS", it.name) }
        return inflater.inflate(R.layout.fragment_forum, container, false)
    }
}
