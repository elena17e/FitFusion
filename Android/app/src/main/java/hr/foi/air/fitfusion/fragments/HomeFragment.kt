package hr.foi.air.fitfusion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.adapters.TrainerHomepageAdapter
import android.content.Intent
import android.widget.ImageButton
import hr.foi.air.fitfusion.TrainerDetailsActivity
import hr.foi.air.fitfusion.WelcomeActivity
import hr.foi.air.fitfusion.adapters.TrainingHomepageAdapter
import hr.foi.air.fitfusion.data_classes.FirebaseManager
import hr.foi.air.fitfusion.data_classes.TrainingModel

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainingsArrayListModel: ArrayList<TrainingModel>

    lateinit var trainingsRecycleView: RecyclerView
    private val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tag = this
        recyclerView = view.findViewById(R.id.trainers_homepage)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        firebaseManager.getTrainers { trainers ->
            recyclerView.adapter = TrainerHomepageAdapter(trainers) { clickedTrainer ->
                val intent = Intent(requireContext(), TrainerDetailsActivity::class.java).apply {
                    putExtra("firstName", clickedTrainer.firstName)
                    putExtra("lastName", clickedTrainer.lastName)
                    putExtra("email", clickedTrainer.email)
                    putExtra("description", clickedTrainer.description)
                }
                startActivity(intent)
            }
        }


        trainingsRecycleView = view.findViewById(R.id.trainings_homepage)
        trainingsRecycleView.layoutManager = LinearLayoutManager(context)

        trainingsArrayListModel = arrayListOf()
        firebaseManager.getTrainings(requireContext(), trainingsRecycleView)
        trainingsRecycleView.adapter = TrainingHomepageAdapter(trainingsArrayListModel, {

        }) { trainingModel ->
            firebaseManager.removeParticipant(trainingModel, requireContext())
        }

        val addButton = view.findViewById<ImageButton>(R.id.addTraining)
        addButton.setOnClickListener{
            navigateToCalendarTab()
        }
    }

    private fun navigateToCalendarTab(){
        (activity as? WelcomeActivity)?.navigateToCalendarTab()
    }
}