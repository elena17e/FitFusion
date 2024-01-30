package hr.foi.air.fitfusion.fragments

import android.annotation.SuppressLint
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
    private lateinit var passedTrainingsList: ArrayList<TrainingModel>
    private lateinit var adapter: TrainingHomepageAdapter

    private lateinit var trainingsRecycleView: RecyclerView
    private lateinit var passedTrainingsRecycleView: RecyclerView
    private val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        adapter = TrainingHomepageAdapter(trainingsArrayListModel, {
            navigateToCalendarTab()
        }) {trainingModel ->
            firebaseManager.removeParticipant(trainingModel, requireContext()) { success ->
                if (success) {
                    trainingsArrayListModel.remove(trainingModel)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        trainingsRecycleView.adapter = adapter

        firebaseManager.getTrainings(requireContext()) {trainings ->
            trainingsArrayListModel.clear()
            trainingsArrayListModel.addAll(trainings)
            adapter.notifyDataSetChanged()
        }

        val addButton = view.findViewById<ImageButton>(R.id.addTraining)
        addButton.setOnClickListener{
            navigateToCalendarTab()
        }

        passedTrainingsRecycleView = view.findViewById(R.id.passed_classes_homepage)
        passedTrainingsRecycleView.layoutManager = LinearLayoutManager(view.context)

        passedTrainingsList = ArrayList()
        firebaseManager.getPassedTrainings(requireContext(), passedTrainingsRecycleView)
    }

    private fun navigateToCalendarTab(){
        (activity as? WelcomeActivity)?.navigateToCalendarTab()
    }
}