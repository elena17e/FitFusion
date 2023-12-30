package hr.foi.air.fitfusion.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.foi.air.fitfusion.R
import android.widget.EditText
import android.widget.Button
import hr.foi.air.fitfusion.WelcomeTrainerActivity
import android.text.Editable
import androidx.appcompat.widget.SwitchCompat


class TrainingSessionDetailsFragment : Fragment() {
    private lateinit var typeEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var participantsEditText: EditText
    private lateinit var switchCanceled: SwitchCompat
    private lateinit var editButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.training_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        typeEditText = view.findViewById(R.id.typeEditText)
        dateEditText = view.findViewById(R.id.dateEditText)
        timeEditText = view.findViewById(R.id.timeEditText)
        participantsEditText = view.findViewById(R.id.participantsEditText)
        switchCanceled = view.findViewById(R.id.switch1)
        editButton = view.findViewById(R.id.Edit)
        cancelButton = view.findViewById(R.id.Cancel)

        val args = arguments
        if (args != null) {
            val type = args.getString("type")
            val date = args.getString("date")
            val time = args.getString("time")
            val participants = args.getString("participants")

            typeEditText.setText(type)
            dateEditText.setText(date)
            timeEditText.setText(time)
            participantsEditText.setText(participants)
        }

        switchCanceled.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                
            }else{

            }
        }

        editButton.setOnClickListener {
        }

        cancelButton.setOnClickListener {
            val intent = Intent(requireContext(), WelcomeTrainerActivity::class.java)
            startActivity(intent)
        }
    }
}
