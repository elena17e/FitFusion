package hr.foi.air.fitfusion.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.foi.air.fitfusion.R
import android.widget.TextView
import android.widget.Switch
import android.widget.Button
import hr.foi.air.fitfusion.WelcomeTrainerActivity

class TrainingSessionDetailsFragment : Fragment() {
    private lateinit var typeTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var participantsTextView: TextView
    private lateinit var switchCanceled: Switch
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.training_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        typeTextView = view.findViewById(R.id.Type)
        dateTextView = view.findViewById(R.id.textView2)
        timeTextView = view.findViewById(R.id.textView3)
        participantsTextView = view.findViewById(R.id.textView4)
        switchCanceled = view.findViewById(R.id.switch1)
        editButton = view.findViewById(R.id.Edit)
        cancelButton = view.findViewById(R.id.Cancel)

        val args = arguments
        if (args != null) {
            val args = requireArguments()
            val type = args.getString("type")
            val date = args.getString("date")
            val time = args.getString("time")
            val participants = args.getString("participants")

            typeTextView.text = "Type: $type"
            dateTextView.text = "Date: $date"
            timeTextView.text = "Time: $time"
            participantsTextView.text = "Participants: $participants"
        }

        editButton.setOnClickListener {
        }

        cancelButton.setOnClickListener {
            val intent = Intent(requireContext(), WelcomeTrainerActivity::class.java)
            startActivity(intent)
        }
    }
}
